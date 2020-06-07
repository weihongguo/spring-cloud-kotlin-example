package com.example.base.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Configuration
class FeignConfig : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val token = requestTemplate.headers().containsKey("Authorization")
        if (!token) {
            RequestContextHolder.getRequestAttributes()?.let {
                val attributes = it as ServletRequestAttributes
                val authorization = attributes.request.getHeader("Authorization")
                requestTemplate.header("Authorization", authorization)
            }
        }
    }
}