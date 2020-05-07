package com.example.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

abstract class BaseAuthenticationConfig : WebSecurityConfigurerAdapter() {

    protected open fun permitMatchers(): Array<String> {
        return arrayOf()
    }

    override fun configure(http: HttpSecurity) {
        http.csrf()
            .disable()
            .cors()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests().antMatchers(*permitMatchers()).permitAll().anyRequest().authenticated()
            /*
            .and()
            .addFilter(getAuthenticationFilter())
            .exceptionHandling()
            .authenticationEntryPoint(getAuthenticationEntryPoint())
            .accessDeniedHandler(getAccessDeniedHandler())
            */
    }
}