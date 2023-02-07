package io.github.gilbertoowen.request.logging.filter;

import io.github.gilbertoowen.request.logging.wrapper.SpringRequestWrapper;
import io.github.gilbertoowen.request.logging.wrapper.SpringResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SpringLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLoggingFilter.class);
    private List<String> ignoreUrl;
    private boolean logHeaders;
    protected static final PathMatcher MATCHER = new AntPathMatcher();

    @Autowired
    ApplicationContext context;

    public SpringLoggingFilter(List<String> ignoreUrl, boolean logHeaders) {
        this.ignoreUrl = ignoreUrl;
        this.logHeaders = logHeaders;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (ignoreUrl != null && ignoreUrl.stream().anyMatch(p -> MATCHER.match(p, request.getRequestURI()))) {
            chain.doFilter(request, response);
        } else {
            final long startTime = System.currentTimeMillis();
            final SpringRequestWrapper wrappedRequest = new SpringRequestWrapper(request);
            if (logHeaders)
                LOGGER.info("request start: method={}, uri={}, payload={}, headers={}", wrappedRequest.getMethod(),
                        wrappedRequest.getRequestURI(), IOUtils.toString(wrappedRequest.getInputStream(),
                        wrappedRequest.getCharacterEncoding()), wrappedRequest.getAllHeaders());
            else
                LOGGER.info("request start: method={}, uri={}, payload={}", wrappedRequest.getMethod(),
                        wrappedRequest.getRequestURI(), IOUtils.toString(wrappedRequest.getInputStream(),
                        wrappedRequest.getCharacterEncoding()));
            final SpringResponseWrapper wrappedResponse = new SpringResponseWrapper(response);

            try {
                chain.doFilter(wrappedRequest, wrappedResponse);
            } catch (Exception e) {
                logResponse(startTime, wrappedResponse, 500);
                throw e;
            }
            logResponse(startTime, wrappedResponse, wrappedResponse.getStatus());
        }
    }

    private void logResponse(long startTime, SpringResponseWrapper wrappedResponse, int overriddenStatus) throws IOException {
        final long duration = System.currentTimeMillis() - startTime;
        wrappedResponse.setCharacterEncoding("UTF-8");
        if (logHeaders)
            LOGGER.info("request end({} ms): status={}, payload={}, headers={}", duration, overriddenStatus,
                    IOUtils.toString(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding()),
                    wrappedResponse.getAllHeaders());
        else
            LOGGER.info("request end({} ms): status={}, payload={}", duration, overriddenStatus,
                    IOUtils.toString(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding()));
    }
}
