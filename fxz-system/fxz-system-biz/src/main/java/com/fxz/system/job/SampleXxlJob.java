package com.fxz.system.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/13 20:36
 */
@Slf4j
@Component
public class SampleXxlJob {

	@XxlJob("demoJobHandler")
	public void demoJobHandler() throws Exception {
		XxlJobHelper.log("XXL-JOB, Hello World.");
		log.info("hello xxl-job");
	}

}
