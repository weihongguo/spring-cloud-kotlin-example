package com.example.account.security

import com.example.security.BasePermissionConfig
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 16:06
 **/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class AccountPermissionConfig : BasePermissionConfig()