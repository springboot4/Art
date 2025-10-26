package com.art.ai.service.conversation.variable;

import com.art.ai.dao.dataobject.AiConversationStateDO;
import com.art.ai.manager.AiConversationStateManager;
import com.art.common.lock.core.constants.RedissonLockType;
import com.art.common.lock.core.entity.LockEntity;
import com.art.common.lock.core.factory.RedissonLockServiceFactory;
import com.art.common.lock.core.service.RedissonService;
import com.art.core.common.util.CollectionUtil;
import com.art.json.sdk.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.art.core.common.util.CollectionUtil.safeMap;

/**
 * 会话变量服务实现
 *
 * @author fxz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationVariableServiceImpl implements ConversationVariableService {

	private final AiConversationStateManager conversationStateManager;

	@Override
	public ConversationVariableSnapshot initialize(Long conversationId, Long appId,
			Map<String, Object> declaredDefaults) {
		if (conversationId == null) {
			return new ConversationVariableSnapshot(Collections.emptyMap());
		}

		Map<String, Object> declaration = safeMap(declaredDefaults);
		AiConversationStateDO existed = conversationStateManager.findByConversationId(conversationId);
		Map<String, Object> persisted = existed != null ? readAsMap(existed.getVarsJson()) : Collections.emptyMap();
		Map<String, Object> filteredPersisted = filterByDeclaration(declaration, persisted);
		try {
			validateByDeclaration(declaration, filteredPersisted);
		}
		catch (IllegalArgumentException ex) {
			log.warn("初始化会话变量时发现结构不匹配，conversationId={}，将使用声明默认值", conversationId, ex);
			filteredPersisted = Collections.emptyMap();
		}
		Map<String, Object> merged = merge(declaration, filteredPersisted);

		return new ConversationVariableSnapshot(merged);
	}

	@Override
	public void persist(Long conversationId, Long appId, Map<String, Object> conversationVars) {
		if (conversationId == null) {
			log.debug("conversationId 为空，跳过会话变量持久化");
			return;
		}

		RedissonService redissonService = RedissonLockServiceFactory.getLock(RedissonLockType.REENTRANT);
		LockEntity lockEntity = new LockEntity().setLockName("conversation:variable:persist:" + conversationId);
		redissonService.lock(lockEntity);
		try {
			AiConversationStateDO existed = conversationStateManager.findByConversationId(conversationId);

			if (existed == null) {
				AiConversationStateDO stateDO = new AiConversationStateDO();
				stateDO.setConversationId(conversationId);
				stateDO.setAppId(appId);
				stateDO.setVarsJson(JacksonUtil.toJsonString(safeMap(conversationVars)));
				conversationStateManager.insert(stateDO);
				return;
			}

			if (StringUtils.isNoneBlank(existed.getVarsJson())) {
				Map<String, Object> existedVars = CollectionUtil
					.safeMap(JacksonUtil.parseObject(existed.getVarsJson(), Map.class));
				existedVars.putAll(safeMap(conversationVars));
				conversationVars = existedVars;
			}

			existed.setVarsJson(JacksonUtil.toJsonString(conversationVars));
			existed.setAppId(appId != null ? appId : existed.getAppId());
			conversationStateManager.updateById(existed);
		}
		finally {
			redissonService.unlock(lockEntity);
		}
	}

	@Override
	public Map<String, Object> filterByDeclaration(Map<String, Object> declaration, Map<String, Object> source) {
		if (MapUtils.isEmpty(declaration) || MapUtils.isEmpty(source)) {
			return new LinkedHashMap<>();
		}

		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		declaration.forEach((key, value) -> {
			if (source.containsKey(key)) {
				result.put(key, source.get(key));
			}
		});
		return result;
	}

	@Override
	public void validateByDeclaration(Map<String, Object> declaration, Map<String, Object> toValidate) {
		if (MapUtils.isEmpty(toValidate)) {
			return;
		}

		toValidate.forEach((key, value) -> {
			if (!declaration.containsKey(key)) {
				throw new IllegalArgumentException("尝试写入未声明的会话变量: " + key);
			}

			Object declared = declaration.get(key);
			if (!isTypeCompatible(declared, value)) {
				throw new IllegalArgumentException(String.format("会话变量类型不匹配: key=%s, declared=%s, actual=%s", key,
						resolveTypeName(declared), resolveTypeName(value)));
			}
		});
	}

	private boolean isTypeCompatible(Object declared, Object value) {
		if (value == null || declared == null) {
			return true;
		}
		if (declared instanceof Map<?, ?>) {
			return value instanceof Map<?, ?>;
		}
		if (declared instanceof Iterable<?>) {
			return value instanceof Iterable<?>;
		}
		if (declared instanceof Number) {
			return value instanceof Number;
		}
		if (declared instanceof Boolean) {
			return value instanceof Boolean;
		}
		if (declared instanceof CharSequence) {
			return value instanceof CharSequence;
		}
		return declared.getClass().isInstance(value);
	}

	private String resolveTypeName(Object value) {
		return value == null ? "null" : value.getClass().getSimpleName();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> readAsMap(String json) {
		if (json == null) {
			return Collections.emptyMap();
		}
		return JacksonUtil.parseObject(json, Map.class);
	}

	public static Map<String, Object> merge(Map<String, Object> declaration, Map<String, Object> persisted) {
		LinkedHashMap<String, Object> merged = new LinkedHashMap<>();
		declaration.forEach((key, defaultValue) -> {
			merged.put(key, persisted.getOrDefault(key, defaultValue));
		});
		return merged;
	}

}
