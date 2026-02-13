package com.github.maximovj.rhhub_app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.header")
public class AppHeaderProperties {
    
    private String keyName;
    
    private String keyValue;

}
