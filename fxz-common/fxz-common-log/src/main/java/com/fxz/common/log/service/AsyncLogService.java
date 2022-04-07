package com.fxz.common.log.service;

import cn.hutool.core.bean.BeanUtil;
import com.fxz.system.dto.OperLogDto;
import com.fxz.system.entity.OperLog;
import com.fxz.system.feign.RemoteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步操作日志服务
 *
 * @author fxz
 */
@Service
public class AsyncLogService {

	@Autowired
	private RemoteLogService remoteLogService;

	/**
	 * 保存系统日志记录
	 */
	@Async
	public void saveSysLog(OperLog operLog) {
		OperLogDto operLogDto = new OperLogDto();
		BeanUtil.copyProperties(operLog, operLogDto);
		remoteLogService.add(operLogDto);
	}

}
