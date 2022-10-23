package com.fxz.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author fxz
 */
@Data
@Component
@ConfigurationProperties(prefix = "fxz.common.doc")
public class DocProperties {

	private Map<String, String> services;

}