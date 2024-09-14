package com.business.apply_system.common.exception.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@PropertySource(value = "classpath:error.properties", encoding = "utf-8")
@ConfigurationProperties(prefix = "gj")
public class ErrorProperties {

    private Map<String, String> error = new HashMap<>();

    public String getMsg(String code) {
        return error.getOrDefault(code, "");
    }
}
