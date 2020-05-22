package com.example.security

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Configuration
class FeignAuthorizationConfig : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val authorization = attributes.request.getHeader(KEY_AUTHORIZATION)
        requestTemplate.header(KEY_AUTHORIZATION, authorization)
    }
}