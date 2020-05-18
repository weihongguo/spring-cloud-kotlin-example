package com.example.security

import com.alibaba.fastjson.JSON
import com.example.base.*
import com.example.database.entity.EntityNotFoundException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class BaseAuthenticationConfig : WebSecurityConfigurerAdapter() {

    protected open fun permitMatchers(): Array<String> {
        return arrayOf()
    }

    abstract fun getAuthorizationService(): AuthorizationService

    @Throws(Exception::class)
    protected open fun getAuthenticationFilter(): BasicAuthenticationFilter? {
        return JwtAuthenticationFilter(authenticationManager(), getAuthorizationService())
    }

    protected open fun getAuthenticationEntryPoint(): AuthenticationEntryPoint? {
        return BaseAuthenticationEntryPoint()
    }

    override fun configure(http: HttpSecurity) {
        http.csrf()
            .disable()
            .cors()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests().antMatchers(*permitMatchers()).permitAll().anyRequest().authenticated()
            .and()
            .addFilter(getAuthenticationFilter())
            .exceptionHandling()
            .authenticationEntryPoint(getAuthenticationEntryPoint())
    }
}

class BaseAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        e: AuthenticationException
    ) {
        val response = unauthorizedResponse(mapOf(
            "exception" to e.toString(),
            "url" to servletRequest.requestURI.toString()
        ))
        servletResponse.setHeader("Content-Type", "application/json;charset=utf-8")
        servletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        servletResponse.writer.write(JSON.toJSONString(response))
        servletResponse.writer.flush()
    }
}

abstract class BaseExceptionHandler {

    @ExceptionHandler(AccessDeniedException::class)
    open fun accessDeniedException(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        e: AccessDeniedException
    ): Response {
        servletResponse.status = HttpServletResponse.SC_FORBIDDEN
        return forbiddenResponse(mapOf(
            "url" to servletRequest.requestURL.toString(),
            "exception" to e.toString()
        ))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    open fun notFoundException(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        e: EntityNotFoundException
    ): Response {
        servletResponse.status = HttpServletResponse.SC_NOT_FOUND
        return notFoundResponse(e.message ?: "数据不存在", mapOf(
            "url" to servletRequest.requestURL.toString(),
            "exception" to e.toString()
        ))
    }

    @ExceptionHandler
    open fun unknownException(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        e: Exception
    ): Response {
        servletResponse.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        return errorResponse(e.message ?: "服务繁忙，请稍后再试", mapOf(
            "url" to servletRequest.requestURL.toString(),
            "exception" to e.toString()
        ))
    }
}