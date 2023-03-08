package io.github.gilbertoowen.request.logging.config;


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
     * white url list, has higher priority than the black list, support ant path style patterns
     */
    private List<String> whiteList;
    /**
     * black url list, support ant path style patterns
     */
    private List<String> blackList;
    /**
     * log request headers
     */
    private boolean logHeaders;

    @Bean
    public SpringLoggingFilter reactiveSpringLoggingFilter() {
        return new SpringLoggingFilter(whiteList, blackList, logHeaders);
    }

    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    public boolean isLogHeaders() {
        return logHeaders;
    }

    public void setLogHeaders(boolean logHeaders) {
        this.logHeaders = logHeaders;
    }
}
