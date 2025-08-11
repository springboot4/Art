package com.art.ai.service.workflow.callback;

/**
 * @author fxz
 * @since 2025/8/11 13:03
 */
@FunctionalInterface
public interface Callback {

	void execute(CallbackResult callbackResult);

}
