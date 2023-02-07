package io.github.gilbertoowen.request.logging.cofig;


import io.github.gilbertoowen.request.logging.filter.SpringLoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "request.logging")
@ConditionalOnProperty(prefix = "request.logging", name = "enabled", havingValue = "true")
public class RequestLoggingConfig {
    /**
     * ignore url, support ant path style patterns
     */
    private List<String> ignoreUrl;
    /**
     * log request headers
     */
    private boolean logHeaders;

    @Bean
    public SpringLoggingFilter reactiveSpringLoggingFilter() {
        return new SpringLoggingFilter(ignoreUrl, logHeaders);
    }

    public List<String> getIgnoreUrl() {
        return ignoreUrl;
    }

    public void setIgnoreUrl(List<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    public boolean isLogHeaders() {
        return logHeaders;
    }

    public void setLogHeaders(boolean logHeaders) {
        this.logHeaders = logHeaders;
    }
}
