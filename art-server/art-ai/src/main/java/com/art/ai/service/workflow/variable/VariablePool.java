package com.art.ai.service.workflow.variable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.art.ai.service.workflow.variable.VariableType.CONVERSATION;

/**
 * @author fxz
 */
@NoArgsConstructor
@ToString
@Setter
@Getter
public class VariablePool implements Serializable {

	private transient final ReadWriteLock lock = new ReentrantReadWriteLock();

	private final Map<String, Map<String, VariableValue<?>>> variableStorage = new ConcurrentHashMap<>();

	/**
	 * 初始化变量池
	 */
	public static VariablePool create(Map<SystemVariableKey, Object> systemVars, Map<String, Object> envVars,
			Map<String, Object> conversationVars, Map<String, Object> userInputs) {
		var pool = new VariablePool();

		if (systemVars != null) {
			systemVars.forEach((key, value) -> {
				pool.addInternal(VariableSelector.system(key), value, true);
			});
		}

		if (envVars != null) {
			envVars.forEach((name, value) -> {
				pool.addInternal(VariableSelector.environment(name), value, true);
			});
		}

		if (conversationVars != null) {
			conversationVars.forEach((name, value) -> {
				pool.addInternal(VariableSelector.conversation(name), value, false);
			});
		}

		if (userInputs != null) {
			userInputs.forEach((name, value) -> {
				pool.addInternal(VariableSelector.of(VariableType.USER_INPUT.getType(), name), value, true);
			});
		}

		return pool;
	}

	/**
	 * 添加变量
	 */
	public void add(VariableSelector selector, Object value) {
		validateWritePermission(selector);
		addInternal(selector, value, isReadOnlyVariable(selector));
	}

	/**
	 * 获取变量
	 */
	public Optional<VariableValue<?>> get(VariableSelector selector) {
		lock.readLock().lock();
		try {
			var nodeStorage = variableStorage.get(selector.nodeId());
			if (nodeStorage == null) {
				return Optional.empty();
			}
			return Optional.ofNullable(nodeStorage.get(selector.getKey()));
		}
		finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 获取变量值
	 */
	public <T> Optional<T> getValue(VariableSelector selector, Class<T> type) {
		return get(selector).map(VariableValue::getValue).filter(type::isInstance).map(type::cast);
	}

	/**
	 * 更新变量（仅可写变量）
	 */
	public void update(VariableSelector selector, Object newValue) {
		var existingVar = get(selector).orElseThrow(() -> new IllegalArgumentException("变量不存在: " + selector));

		if (existingVar.isReadOnly()) {
			throw new IllegalStateException("变量为只读: " + selector);
		}

		addInternal(selector, newValue, false);
	}

	private void addInternal(VariableSelector selector, Object value, boolean readOnly) {
		lock.writeLock().lock();
		try {
			var nodeStorage = variableStorage.computeIfAbsent(selector.nodeId(), k -> new ConcurrentHashMap<>());
			var variableValue = createVariableValue(value, readOnly);
			nodeStorage.put(selector.getKey(), variableValue);
		}
		finally {
			lock.writeLock().unlock();
		}
	}

	public Map<String, Object> snapshotConversationVariables() {
		lock.readLock().lock();
		try {
			var conversationVariables = variableStorage.computeIfAbsent(CONVERSATION.getType(),
					k -> new ConcurrentHashMap<>());
			return new ConcurrentHashMap<>(conversationVariables);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	private void validateWritePermission(VariableSelector selector) {
		if (isReadOnlyVariable(selector)) {
			throw new IllegalStateException("变量为只读: " + selector);
		}
	}

	private boolean isReadOnlyVariable(VariableSelector selector) {
		return VariableType.SYSTEM.getType().equals(selector.nodeId())
				|| VariableType.ENVIRONMENT.getType().equals(selector.nodeId())
				|| VariableType.USER_INPUT.getType().equals(selector.nodeId());
	}

	private VariableValue<?> createVariableValue(Object value, boolean readOnly) {
		if (value == null) {
			return new VariableValue.StringVariableValue(null, readOnly);
		}
		else if (value instanceof String) {
			return new VariableValue.StringVariableValue((String) value, readOnly);
		}
		else if (value instanceof Number) {
			return new VariableValue.NumberVariableValue((Number) value, readOnly);
		}
		else if (value instanceof Boolean) {
			return new VariableValue.BooleanVariableValue((Boolean) value, readOnly);
		}
		else if (value instanceof List<?>) {
			return new VariableValue.ArrayVariableValue((List<?>) value, readOnly);
		}
		else if (value instanceof java.io.File) {
			return new VariableValue.FileVariableValue((java.io.File) value, readOnly);
		}
		else {
			return new VariableValue.ObjectVariableValue(value, readOnly);
		}
	}

}