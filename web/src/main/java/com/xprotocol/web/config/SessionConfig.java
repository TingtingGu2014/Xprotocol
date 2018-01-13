package com.xprotocol.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class SessionConfig  extends AbstractHttpSessionApplicationInitializer {
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderCookieHttpSessionStrategy();
    }
}
