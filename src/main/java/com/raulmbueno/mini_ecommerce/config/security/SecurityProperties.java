package com.raulmbueno.mini_ecommerce.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.security.token")
@Getter
@Setter
public class SecurityProperties {
    private String secret;
    private int expirationHours;    
}
