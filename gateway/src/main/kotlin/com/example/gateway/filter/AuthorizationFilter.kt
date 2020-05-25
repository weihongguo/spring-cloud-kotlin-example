package com.example.gateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthorizationFilter : GlobalFilter, Ordered {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val rawPath = request.uri.rawPath
        log.info("$rawPath : auth start")
        val authorization = request.headers.getFirst("Authorization")
        log.info("$rawPath + $authorization")
        if (authorization == null || authorization.isBlank()) {
            log.info("$rawPath : auth fail")
            val response = exchange.response
            response.statusCode = HttpStatus.UNAUTHORIZED
            return response.setComplete()
        }
        return chain.filter(exchange).then(Mono.fromRunnable {
            log.info("$rawPath : auth after")
        })
    }

    override fun getOrder(): Int {
        return -100
    }
}