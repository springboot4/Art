/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.dataPermission.db;

import cn.hutool.core.collection.CollUtil;
import com.art.common.dataPermission.factory.DataPermissionRuleFactory;
import com.art.common.dataPermission.local.DataPermissionRuleContextHolder;
import com.art.common.dataPermission.local.MappedStatementCache;
import com.art.common.dataPermission.rule.DataPermissionRule;
import com.art.mybatis.common.utils.MyBatisUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据权限拦截器 通过数据权限规则 重写SQL的方式来实现
 * <p/>
 * 主要的SQL重写方法可见 {@link #builderExpression(Expression, Table)} 方法 参考
 * {@link com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor} 实现
 *
 * @author fxz
 */
@RequiredArgsConstructor
public class DataPermissionDatabaseInterceptor extends JsqlParserSupport implements InnerInterceptor {

	/**
	 * 数据权限规则工厂接口 管理容器中配置的数据权限规则
	 */
	private final DataPermissionRuleFactory ruleFactory;

	private final MappedStatementCache mappedStatementCache = new MappedStatementCache();

	/**
	 * Executor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey,
	 * BoundSql) 操作前置处理 改改sql啥的
	 */
	@Override
	public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) {
		// 获取生效的数据权限规则
		List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule();

		// 如果无需重写 则跳过
		if (mappedStatementCache.noRewritable(ms, rules)) {
			return;
		}

		PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
		try {
			// 初始化上下文
			DataPermissionRuleContextHolder.init(rules);
			// 处理 SQL
			mpBs.sql(parserSingle(mpBs.sql(), null));
		}
		finally {
			addMappedStatementCache(ms);
			DataPermissionRuleContextHolder.clear();
		}
	}

	/**
	 * 只处理 UPDATE / DELETE 场景 不处理 INSERT 场景
	 */
	@Override
	public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		SqlCommandType sct = ms.getSqlCommandType();
		if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
			// 获取生效的数据权限规则
			List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule();
			// 如果无需重写，则跳过
			if (mappedStatementCache.noRewritable(ms, rules)) {
				return;
			}

			PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
			try {
				// 初始化上下文
				DataPermissionRuleContextHolder.init(rules);
				// 处理 SQL
				mpBs.sql(parserMulti(mpBs.sql(), null));
			}
			finally {
				addMappedStatementCache(ms);
				DataPermissionRuleContextHolder.clear();
			}
		}
	}

	@Override
	protected void processSelect(Select select, int index, String sql, Object obj) {
		processSelectBody(select.getSelectBody());
		List<WithItem> withItemsList = select.getWithItemsList();
		if (!CollectionUtils.isEmpty(withItemsList)) {
			withItemsList.forEach(this::processSelectBody);
		}
	}

	protected void processSelectBody(SelectBody selectBody) {
		if (selectBody == null) {
			return;
		}
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody);
		}
		else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			processSelectBody(withItem.getSubSelect().getSelectBody());
		}
		else {
			SetOperationList operationList = (SetOperationList) selectBody;
			List<SelectBody> selectBodys = operationList.getSelects();
			if (CollectionUtils.isNotEmpty(selectBodys)) {
				selectBodys.forEach(this::processSelectBody);
			}
		}
	}

	/**
	 * update 语句处理
	 */
	@Override
	protected void processUpdate(Update update, int index, String sql, Object obj) {
		final Table table = update.getTable();
		update.setWhere(this.builderExpression(update.getWhere(), table));
	}

	/**
	 * delete 语句处理
	 */
	@Override
	protected void processDelete(Delete delete, int index, String sql, Object obj) {
		delete.setWhere(this.builderExpression(delete.getWhere(), delete.getTable()));
	}

	/**
	 * 处理 PlainSelect
	 */
	protected void processPlainSelect(PlainSelect plainSelect) {
		FromItem fromItem = plainSelect.getFromItem();
		Expression where = plainSelect.getWhere();
		processWhereSubSelect(where);
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			plainSelect.setWhere(builderExpression(where, fromTable));
		}
		else {
			processFromItem(fromItem);
		}
		// #3087 github
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		if (CollectionUtils.isNotEmpty(selectItems)) {
			selectItems.forEach(this::processSelectItem);
		}
		List<Join> joins = plainSelect.getJoins();
		if (CollectionUtils.isNotEmpty(joins)) {
			processJoins(joins);
		}
	}

	/**
	 * 处理where条件内的子查询
	 * <p>
	 * 支持如下: 1. in 2. = 3. > 4. < 5. >= 6. <= 7. <> 8. EXISTS 9. NOT EXISTS
	 * <p>
	 * 前提条件: 1. 子查询必须放在小括号中 2. 子查询一般放在比较操作符的右边
	 * @param where where 条件
	 */
	protected void processWhereSubSelect(Expression where) {
		if (where == null) {
			return;
		}
		if (where instanceof FromItem) {
			processFromItem((FromItem) where);
			return;
		}
		if (where.toString().indexOf("SELECT") > 0) {
			// 有子查询
			if (where instanceof BinaryExpression) {
				// 比较符号 , and , or , 等等
				BinaryExpression expression = (BinaryExpression) where;
				processWhereSubSelect(expression.getLeftExpression());
				processWhereSubSelect(expression.getRightExpression());
			}
			else if (where instanceof InExpression) {
				// in
				InExpression expression = (InExpression) where;
				ItemsList itemsList = expression.getRightItemsList();
				if (itemsList instanceof SubSelect) {
					processSelectBody(((SubSelect) itemsList).getSelectBody());
				}
			}
			else if (where instanceof ExistsExpression) {
				// exists
				ExistsExpression expression = (ExistsExpression) where;
				processWhereSubSelect(expression.getRightExpression());
			}
			else if (where instanceof NotExpression) {
				// not exists
				NotExpression expression = (NotExpression) where;
				processWhereSubSelect(expression.getExpression());
			}
			else if (where instanceof Parenthesis) {
				Parenthesis expression = (Parenthesis) where;
				processWhereSubSelect(expression.getExpression());
			}
		}
	}

	protected void processSelectItem(SelectItem selectItem) {
		if (selectItem instanceof SelectExpressionItem) {
			SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
			if (selectExpressionItem.getExpression() instanceof SubSelect) {
				processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
			}
			else if (selectExpressionItem.getExpression() instanceof Function) {
				processFunction((Function) selectExpressionItem.getExpression());
			}
		}
	}

	/**
	 * 处理函数
	 * <p>
	 * 支持: 1. select fun(args..) 2. select fun1(fun2(args..),args..)
	 * <p>
	 * <p>
	 * fixed gitee pulls/141
	 * </p>
	 * @param function 函数
	 */
	protected void processFunction(Function function) {
		ExpressionList parameters = function.getParameters();
		if (parameters != null) {
			parameters.getExpressions().forEach(expression -> {
				if (expression instanceof SubSelect) {
					processSelectBody(((SubSelect) expression).getSelectBody());
				}
				else if (expression instanceof Function) {
					processFunction((Function) expression);
				}
			});
		}
	}

	/**
	 * 处理子查询等
	 */
	protected void processFromItem(FromItem fromItem) {
		if (fromItem instanceof SubJoin) {
			SubJoin subJoin = (SubJoin) fromItem;
			if (subJoin.getJoinList() != null) {
				processJoins(subJoin.getJoinList());
			}
			if (subJoin.getLeft() != null) {
				processFromItem(subJoin.getLeft());
			}
		}
		else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody());
			}
		}
		else if (fromItem instanceof ValuesList) {
			logger.debug("Perform a subquery, if you do not give us feedback");
		}
		else if (fromItem instanceof LateralSubSelect) {
			LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
			if (lateralSubSelect.getSubSelect() != null) {
				SubSelect subSelect = lateralSubSelect.getSubSelect();
				if (subSelect.getSelectBody() != null) {
					processSelectBody(subSelect.getSelectBody());
				}
			}
		}
	}

	/**
	 * 处理 joins
	 * @param joins join 集合
	 */
	private void processJoins(List<Join> joins) {
		// 对于 on 表达式写在最后的 join，需要记录下前面多个 on 的表名
		Deque<Table> tables = new LinkedList<>();
		for (Join join : joins) {
			// 处理 on 表达式
			FromItem fromItem = join.getRightItem();
			if (fromItem instanceof Table) {
				Table fromTable = (Table) fromItem;
				// 获取 join 尾缀的 on 表达式列表
				Collection<Expression> originOnExpressions = join.getOnExpressions();
				// 正常 join on 表达式只有一个，立刻处理
				if (originOnExpressions.size() == 1) {
					processJoin(join);
					continue;
				}
				tables.push(fromTable);
				// 尾缀多个 on 表达式的时候统一处理
				if (originOnExpressions.size() > 1) {
					Collection<Expression> onExpressions = new LinkedList<>();
					for (Expression originOnExpression : originOnExpressions) {
						Table currentTable = tables.poll();
						onExpressions.add(builderExpression(originOnExpression, currentTable));
					}
					join.setOnExpressions(onExpressions);
				}
			}
			else {
				// 处理右边连接的子表达式
				processFromItem(fromItem);
			}
		}
	}

	/**
	 * 处理联接语句
	 */
	protected void processJoin(Join join) {
		if (join.getRightItem() instanceof Table) {
			Table fromTable = (Table) join.getRightItem();
			Expression originOnExpression = CollUtil.getFirst(join.getOnExpressions());
			originOnExpression = builderExpression(originOnExpression, fromTable);
			join.setOnExpressions(CollUtil.newArrayList(originOnExpression));
		}
	}

	/**
	 * 处理条件
	 */
	protected Expression builderExpression(Expression currentExpression, Table table) {
		// 获得 Table 对应的数据权限条件
		Expression equalsTo = buildDataPermissionExpression(table);
		if (equalsTo == null) { // 如果没条件，则返回 currentExpression 默认
			return currentExpression;
		}

		// 表达式为空，则直接返回 equalsTo
		if (currentExpression == null) {
			return equalsTo;
		}
		// 如果表达式为 Or，则需要 (currentExpression) AND equalsTo
		if (currentExpression instanceof OrExpression) {
			return new AndExpression(new Parenthesis(currentExpression), equalsTo);
		}
		// 如果表达式为 And，则直接返回 currentExpression AND equalsTo
		return new AndExpression(currentExpression, equalsTo);
	}

	/**
	 * 构建指定表的数据权限的 Expression 过滤条件
	 * @param table 表
	 * @return Expression 过滤条件
	 */
	private Expression buildDataPermissionExpression(Table table) {
		// 生成条件
		Expression allExpression = null;
		for (DataPermissionRule rule : DataPermissionRuleContextHolder.getRules()) {
			// 判断表名是否匹配
			if (!rule.getTableNames().contains(table.getName())) {
				continue;
			}
			// 如果有匹配的规则，说明可重写。
			// 为什么不是有 allExpression 非空才重写呢？在生成 column = value 过滤条件时，会因为 value 不存在，导致未重写。
			// 这样导致第一次无 value，被标记成无需重写；但是第二次有 value，此时会需要重写。
			DataPermissionRuleContextHolder.setRewrite(true);

			// 单条规则的条件
			String tableName = MyBatisUtils.getTableName(table);
			Expression oneExpress = rule.getExpression(tableName, table.getAlias());
			// 拼接到 allExpression 中
			allExpression = allExpression == null ? oneExpress : new AndExpression(allExpression, oneExpress);
		}

		return allExpression;
	}

	/**
	 * 判断 SQL 是否重写 如果没有重写 则添加到 {@link MappedStatementCache} 中
	 * @param ms MappedStatement
	 */
	private void addMappedStatementCache(MappedStatement ms) {
		if (DataPermissionRuleContextHolder.getRewrite()) {
			return;
		}
		// 不重写 进行添加
		mappedStatementCache.addNoRewritable(ms, DataPermissionRuleContextHolder.getRules());
	}

}
