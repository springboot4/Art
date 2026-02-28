package com.art.ai.service.model.runtime;

import com.art.ai.core.constants.ModelFeature;
import com.art.ai.dao.dataobject.AiModelDO;
import com.art.ai.dao.mysql.AiModelMapper;
import com.art.ai.dto.audio.VoiceInfo;
import com.art.ai.service.model.audio.Speech2TextModel;
import com.art.ai.service.model.audio.TTSModel;
import com.art.ai.service.model.runtime.protocol.AudioModelProtocolHandler;
import com.art.ai.service.model.support.AiModelInvokeOptions;
import com.art.ai.service.model.support.AiModelRuntimeContext;
import com.art.core.common.exception.ArtException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 音频模型运行时服务
 *
 * @author fxz
 * @since 2026/02/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AudioModelRuntimeService {

	private final AiModelRuntimeService aiModelRuntimeService;

	private final List<AudioModelProtocolHandler> audioHandlers;

	private final AiModelMapper aiModelMapper;

	/**
	 * 获取语音转文字模型
	 * @param platformId 平台 ID
	 * @param modelId 模型 ID
	 * @param options 调用选项
	 * @return STT 模型实例
	 */
	public Speech2TextModel acquireSpeech2TextModel(Long platformId, Long modelId, AiModelInvokeOptions options) {
		AiModelRuntimeContext context = aiModelRuntimeService.resolveContext(platformId, modelId);

		// 检查模型是否支持语音转文字特性
		if (!context.getModel().hasFeature(ModelFeature.SPEECH_TO_TEXT)) {
			throw new ArtException(String.format("模型 %s 不支持语音转文字功能", modelId));
		}

		AudioModelProtocolHandler handler = audioHandlers.stream()
			.filter(h -> h.supportsSpeech2Text(context))
			.findFirst()
			.orElseThrow(
					() -> new ArtException(String.format("未找到语音转文字协议处理器, protocol=%s", context.getProtocolType())));

		AiModelInvokeOptions effectiveOptions = options == null ? AiModelInvokeOptions.empty() : options;
		return handler.createSpeech2TextModel(context, effectiveOptions);
	}

	/**
	 * 获取文字转语音模型
	 * @param platformId 平台 ID
	 * @param modelId 模型 ID
	 * @param options 调用选项
	 * @return TTS 模型实例
	 */
	public TTSModel acquireTTSModel(Long platformId, Long modelId, AiModelInvokeOptions options) {
		AiModelRuntimeContext context = aiModelRuntimeService.resolveContext(platformId, modelId);

		// 检查模型是否支持文字转语音特性
		if (!context.getModel().hasFeature(ModelFeature.TEXT_TO_SPEECH)) {
			throw new ArtException(String.format("模型 %s 不支持文字转语音功能", modelId));
		}

		AudioModelProtocolHandler handler = audioHandlers.stream()
			.filter(h -> h.supportsTTS(context))
			.findFirst()
			.orElseThrow(
					() -> new ArtException(String.format("未找到文字转语音协议处理器, protocol=%s", context.getProtocolType())));

		AiModelInvokeOptions effectiveOptions = options == null ? AiModelInvokeOptions.empty() : options;
		return handler.createTTSModel(context, effectiveOptions);
	}

	/**
	 * 获取默认的语音转文字模型
	 * @param tenantId 租户 ID
	 * @return STT 模型实例
	 */
	public Speech2TextModel acquireDefaultSpeech2TextModel(Long tenantId) {
		AiModelRuntimeContext context = resolveDefaultAudioContext(tenantId, ModelFeature.SPEECH_TO_TEXT);

		AudioModelProtocolHandler handler = audioHandlers.stream()
			.filter(h -> h.supportsSpeech2Text(context))
			.findFirst()
			.orElseThrow(() -> new ArtException("未配置默认语音转文字模型"));

		return handler.createSpeech2TextModel(context, AiModelInvokeOptions.empty());
	}

	/**
	 * 获取默认的文字转语音模型
	 * @param tenantId 租户 ID
	 * @return TTS 模型实例
	 */
	public TTSModel acquireDefaultTTSModel(Long tenantId) {
		AiModelRuntimeContext context = resolveDefaultAudioContext(tenantId, ModelFeature.TEXT_TO_SPEECH);

		AudioModelProtocolHandler handler = audioHandlers.stream()
			.filter(h -> h.supportsTTS(context))
			.findFirst()
			.orElseThrow(() -> new ArtException("未配置默认文字转语音模型"));

		return handler.createTTSModel(context, AiModelInvokeOptions.empty());
	}

	/**
	 * 获取可用声音列表
	 * @param platformId 平台 ID
	 * @param modelId 模型 ID
	 * @param language 语言代码
	 * @return 声音列表
	 */
	public List<VoiceInfo> getVoices(Long platformId, Long modelId, String language) {
		TTSModel ttsModel = acquireTTSModel(platformId, modelId, null);
		return ttsModel.getVoices(language);
	}

	/**
	 * 解析默认音频模型上下文
	 * @param tenantId 租户 ID
	 * @param requiredFeature 需要的特性
	 * @return 模型运行时上下文
	 */
	private AiModelRuntimeContext resolveDefaultAudioContext(Long tenantId, ModelFeature requiredFeature) {
		String featureName = requiredFeature == ModelFeature.SPEECH_TO_TEXT ? "语音转文字" : "文字转语音";

		// 查询租户下启用的模型
		List<AiModelDO> models = aiModelMapper
			.selectList(new LambdaQueryWrapper<AiModelDO>().eq(AiModelDO::getEnable, 1));

		if (models.isEmpty()) {
			throw new ArtException("未找到可用的模型，请先配置模型");
		}

		// 查找支持指定特性的模型
		for (AiModelDO model : models) {
			if (model.hasFeature(requiredFeature)) {
				log.info("使用模型 {} 作为默认{}模型", model.getName(), featureName);
				return aiModelRuntimeService.resolveContext(model.getPlatform(), model.getId());
			}
		}

		throw new ArtException("未找到支持" + featureName + "的模型，请在模型配置中添加 " + requiredFeature.name() + " 特性");
	}

}
