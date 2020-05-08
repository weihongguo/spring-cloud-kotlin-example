package com.example.security

import com.alibaba.fastjson.JSON
import com.example.base.response.responseUnauthorized
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
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

    override fun commence(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse, exception: AuthenticationException) {
        val response = responseUnauthorized()
        response.data = mapOf(
            "exception" to exception.toString(),
            "url" to httpRequest.requestURI.toString()
        )
        httpResponse.setHeader("Content-Type", "application/json;charset=utf-8")
        httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        httpResponse.writer.write(JSON.toJSONString(response))
        httpResponse.writer.flush()
    }
}