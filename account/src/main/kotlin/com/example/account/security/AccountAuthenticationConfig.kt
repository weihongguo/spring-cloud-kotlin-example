package com.example.account.security

import com.example.security.BaseAuthenticationConfig
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
class AccountAuthenticationConfig : BaseAuthenticationConfig() {

    override fun permitMatchers(): Array<String> {
        return arrayOf("/init", "/login", "/register", "/test/**")
    }
}