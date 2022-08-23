package com.fxz.gateway.factory;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalTime;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/19 20:57
 */
@Data
@FieldNameConstants
public class FxzTimeBetweenConfig {

	private LocalTime startTime;

	private LocalTime endTime;

}
