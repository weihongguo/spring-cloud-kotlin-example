package com.example.base.feign

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Configuration
class FeignConfig : RequestInterceptor {

    companion object {
        const val KEY_AUTHORIZATION = "Authorization"
    }

    override fun apply(requestTemplate: RequestTemplate) {
        val authorization = requestTemplate.headers().containsKey(KEY_AUTHORIZATION)
        if (!authorization) {
            RequestContextHolder.getRequestAttributes()?.let {
                val attributes = it as ServletRequestAttributes
                requestTemplate.header(KEY_AUTHORIZATION, attributes.request.getHeader(KEY_AUTHORIZATION))
            }
        }
    }
}