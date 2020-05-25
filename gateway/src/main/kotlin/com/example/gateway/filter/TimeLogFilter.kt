package com.example.gateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class TimeLogFilter : GlobalFilter, Ordered {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val startTime = System.currentTimeMillis()
        return chain.filter(exchange).then(Mono.fromRunnable {
            val duration = System.currentTimeMillis() - startTime
            log.info("${exchange.request.uri.rawPath} : $duration ms")
        })
    }

    override fun getOrder(): Int {
        return 100
    }
}