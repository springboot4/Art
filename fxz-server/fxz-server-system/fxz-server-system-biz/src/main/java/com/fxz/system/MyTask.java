package com.fxz.system;

import com.fxz.system.dinger.UserDinger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-22 19:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MyTask {

	private final UserDinger userDinger;

	// @Scheduled(cron = "0/1 * * * * ?")
	public void po() {
		log.info("-----");
		userDinger.success(LocalDateTime.now().toString(), "");
	}

}
