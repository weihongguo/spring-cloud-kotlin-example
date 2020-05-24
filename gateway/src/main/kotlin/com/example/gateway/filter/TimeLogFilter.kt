package com.example.gateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
class TimeLogFilter {
    private val log = LoggerFactory.getLogger(javaClass)
    
    @Bean
    fun timeLogFilter(): GlobalFilter {
        val startTime = System.currentTimeMillis()
        return GlobalFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            chain.filter(exchange).then(Mono.fromRunnable {
                val duration = System.currentTimeMillis() - startTime
                log.info("${exchange.request.uri.rawPath} : $duration ms")
            })
        }
    }
}