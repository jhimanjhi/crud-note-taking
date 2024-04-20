package com.ota.notetaking.logging;

import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@NonNullApi
public class LoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // Log request details
        log.info("Received request: {} {} {}", request.getMethod(), request.getPath(), request.getHeaders());

        return chain.filter(exchange).doAfterTerminate(() -> log.info("Responded with status code: {}", response.getStatusCode()));
    }
}