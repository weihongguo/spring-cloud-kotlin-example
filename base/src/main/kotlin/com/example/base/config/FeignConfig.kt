package com.example.base.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Configuration
class FeignConfig : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val authorization = requestTemplate.headers().containsKey("Authorization")
        if (!authorization) {
            RequestContextHolder.getRequestAttributes()?.let {
                val attributes = it as ServletRequestAttributes
                requestTemplate.header("Authorization", attributes.request.getHeader("Authorization"))
            }
        }
    }
}