package com.fxz.gateway.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author fxz
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:fxz-gateway.properties"})
@ConfigurationProperties(prefix = "fxz.gateway")
public class FxzGatewayProperties {

    /**
     * 禁止外部访问的 URI，多个值的话以逗号分隔
     */
    private String forbidRequestUri;
}