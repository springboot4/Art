package com.fxz.demos.mongo;

import com.fxz.demos.mongo.entity.FxzEmpDocument;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/23 11:20
 */
@Slf4j
@SpringBootTest
public class MongoTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 创建集合
	 */
	@Test
	public void testCollection() {
		if (mongoTemplate.collectionExists("emp")) {
			// 删除集合
			mongoTemplate.dropCollection("emp");
		}

		// 创建集合
		mongoTemplate.createCollection("emp");
	}

	/**
	 * insert方法返回值是新增的Document对象，里面包含了新增后id的值。 如果集合不存在会自动创建集合。 通过Spring Data
	 * MongoDB还会给集合中多加一个class的属性，存储新增时Document对应Java中 类的全限定路径。
	 * 这么做为了查询时能把Document转换为Java类型。
	 */
	@Test
	public void testInsert() {
		FxzEmpDocument empDocument = new FxzEmpDocument(1, "fxz", 30, 10000.00, new Date());

		// sava: _id存在时更新数据
		mongoTemplate.save(empDocument);
		// insert： _id存在抛出异常 支持批量操作
		mongoTemplate.insert(empDocument.setId(999));

		List<FxzEmpDocument> list = Arrays.asList(new FxzEmpDocument(2, "张三", 21, 5000.00, new Date()),
				new FxzEmpDocument(3, "李四", 26, 8000.00, new Date()),
				new FxzEmpDocument(4, "王五", 22, 8000.00, new Date()),
				new FxzEmpDocument(5, "张龙", 28, 6000.00, new Date()),
				new FxzEmpDocument(6, "赵虎", 24, 7000.00, new Date()),
				new FxzEmpDocument(7, "赵六", 28, 12000.00, new Date()));

		// 插入多条数据
		mongoTemplate.insert(list, FxzEmpDocument.class);
	}

	@Test
	public void testFind() {

		log.info("==========查询所有文档===========");
		List<FxzEmpDocument> list = mongoTemplate.findAll(FxzEmpDocument.class);
		list.forEach(i -> log.info("{}", i));

		log.info("==========根据_id查询===========");
		FxzEmpDocument e = mongoTemplate.findById(1, FxzEmpDocument.class);
		log.info("{}", e);

		log.info("==========findOne返回第一个文档===========");
		FxzEmpDocument one = mongoTemplate.findOne(new Query(), FxzEmpDocument.class);
		log.info("{}", one);

		log.info("==========条件查询===========");

		// 查询薪资大于等于8000的员工
		Query query = new Query(Criteria.where("salary").gte(8000));
		// 查询薪资大于4000小于10000的员工
		query = new Query(Criteria.where("salary").gt(4000).lt(10000));
		// 正则查询（模糊查询）
		query = new Query(Criteria.where("name").regex("张"));

		// and or 多条件查询
		Criteria criteria = new Criteria();

		// and 查询年龄大于25&薪资大于8000的员工
		criteria.andOperator(Criteria.where("age").gt(25), Criteria.where("salary").gt(8000));

		// or 查询姓名是张三或者薪资大于8000的员工
		criteria.orOperator(Criteria.where("name").is("张三"), Criteria.where("salary").gt(5000));

		query = new Query(criteria);

		// sort排序
		query.with(Sort.by(Sort.Order.desc("salary")));
		// skip limit 分页 skip用于指定跳过记录数，limit则用于限定返回结果数量。
		query.with(Sort.by(Sort.Order.desc("salary"))).skip(0).limit(4);

		// 查询结果
		List<FxzEmpDocument> employees = mongoTemplate.find(query, FxzEmpDocument.class);
		employees.forEach(i -> log.info("{}", i));
	}

	/**
	 * 使用json字符串方式查询
	 */
	@Test
	public void testFindByJson() {
		// 等值查询
		String json = "{name:'张三'}";
		// 多条件查询
		json = "{$or:[{age:{$gt:25}},{salary:{$gte:8000}}]}";
		Query query = new BasicQuery(json);

		// 查询结果
		List<FxzEmpDocument> employees = mongoTemplate.find(query, FxzEmpDocument.class);
		employees.forEach(i -> log.info("{}", i));
	}

	@Test
	public void testUpdate() {

		// query设置查询条件
		Query query = new Query(Criteria.where("salary").gte(15000));

		log.info("==========更新前===========");
		List<FxzEmpDocument> employees = mongoTemplate.find(query, FxzEmpDocument.class);
		employees.forEach(i -> log.info("{}", i));

		Update update = new Update();
		// 设置更新属性
		update.set("salary", 13000);

		// updateFirst() 只更新满足条件的第一条记录
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, FxzEmpDocument.class);
		// updateMulti() 更新所有满足条件的记录
		updateResult = mongoTemplate.updateMulti(query, update, FxzEmpDocument.class);

		// upsert() 没有符合条件的记录则插入数据
		update.setOnInsert("id", 11);
		// 指定_id
		updateResult = mongoTemplate.upsert(query, update, FxzEmpDocument.class);

		// 返回修改的记录数
		log.info("修改的记录数:{}条", updateResult.getModifiedCount());

		log.info("==========更新后===========");
		employees = mongoTemplate.find(query, FxzEmpDocument.class);
		employees.forEach(i -> log.info("{}", i));
	}

	@Test
	public void testDelete() {
		// 删除所有文档
		mongoTemplate.remove(new Query(), FxzEmpDocument.class);

		// 条件删除
		Query query = new Query(Criteria.where("salary").gte(10000));
		mongoTemplate.remove(query, FxzEmpDocument.class);

	}

}
