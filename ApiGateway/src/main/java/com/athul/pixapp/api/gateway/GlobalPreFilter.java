package com.athul.pixapp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class GlobalPreFilter implements GlobalFilter, Ordered {

    final Logger logger = LoggerFactory.getLogger(GlobalPreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Global pre-filter executed...");
        String requestPath = exchange.getRequest().getPath().toString();
        logger.info("Request path = " + requestPath);
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headerKeys = headers.keySet();
        headerKeys.forEach((headerKey) -> {
            String headerValue = headers.getFirst(headerKey);
            logger.info(headerKey + " " + headerValue);
        });
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
