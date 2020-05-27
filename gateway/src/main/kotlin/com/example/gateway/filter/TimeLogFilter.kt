package com.example.gateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class TimeLogFilterFactory : AbstractGatewayFilterFactory<TimeLogFilterFactory.Config>(), Ordered {

    override fun apply(config: Config): GatewayFilter {
        return TimeLogFilter(config.threshold)
    }

    override fun getOrder(): Int {
        return 100
    }

    class Config (var threshold: Int)
}

class TimeLogFilter(private var threshold: Int) : GatewayFilter {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val startTime = System.currentTimeMillis()
        return chain.filter(exchange).then(Mono.fromRunnable {
            val duration = System.currentTimeMillis() - startTime
            if (duration > threshold) {
                log.info("${exchange.request.uri.rawPath} : $duration ms")
            }
        })
    }
}