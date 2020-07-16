package com.example.base.security

import com.example.base.unauthorizedResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:55
 **/
abstract class BaseAuthenticationConfig : WebSecurityConfigurerAdapter() {

    abstract fun getAuthorizationService(): AuthorizationService

    protected open fun permitMatchers(): Array<String> {
        return arrayOf()
    }

    protected open fun getAuthenticationFilter(): BasicAuthenticationFilter {
        return JwtAuthenticationFilter(authenticationManager(), getAuthorizationService())
    }

    protected open fun getAuthenticationEntryPoint(): AuthenticationEntryPoint {
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
            "from" to "security",
            "exception" to e.toString(),
            "url" to servletRequest.requestURI.toString()
        ))
        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(response);
        servletResponse.setHeader("Content-Type", "application/json;charset=utf-8")
        servletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        servletResponse.writer.write(json)
        servletResponse.writer.flush()
    }
}