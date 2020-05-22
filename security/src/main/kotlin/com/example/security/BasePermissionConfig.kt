package com.example.security

import com.example.database.entity.Permission
import com.example.database.entity.PermissionMethod
import com.fasterxml.jackson.annotation.JsonIgnore
import org.slf4j.LoggerFactory
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.util.AntPathMatcher
import java.io.Serializable

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:55
 **/
abstract class BasePermissionConfig : GlobalMethodSecurityConfiguration() {

    protected fun getPermissionEvaluator(): PermissionEvaluator {
        return BasePermissionEvaluator(AntPathMatcher())
    }

    override fun createExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        expressionHandler.setPermissionEvaluator(getPermissionEvaluator())
        return expressionHandler
    }
}

class BasePermissionEvaluator(private var antPathMatcher: AntPathMatcher) : PermissionEvaluator {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun hasPermission(authentication: Authentication, path: Any, method: Any): Boolean {
        log.info("$method - $path")
        log.info(authentication.authorities.toString())
        val grantedAuthorities = authentication.authorities as Collection<GrantedAuthority>
        for (grantedAuthority in grantedAuthorities) {
            val permissionAuthority = grantedAuthority as PermissionAuthority
            if (permissionAuthority.check(antPathMatcher, path as String, method as String)) {
                return true
            }
        }
        return false
    }

    override fun hasPermission(p0: Authentication?, p1: Serializable?, p2: String?, p3: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class PermissionAuthority(var permission: Permission? = null) : GrantedAuthority {
    private val log = LoggerFactory.getLogger(javaClass)

    @JsonIgnore
    override fun getAuthority(): String {
        return permission?.let {
            return it.name
        } ?: "未知权限"
    }

    fun check(antPathMatcher: AntPathMatcher, path: String, method: String): Boolean {
        permission?.let {
            log.info("${it.name} - ${it.method} - ${it.pathPattern}")
            if (!antPathMatcher.match(it.pathPattern, path)) {
                return false
            }
            if (it.method == PermissionMethod.ALL.value) {
                return true
            } else if (it.method == PermissionMethod.WRITE.value) {
                if (method == PermissionMethod.WRITE.value || method == PermissionMethod.READ.value) {
                    return true
                }
            } else if (it.method == PermissionMethod.READ.value) {
                if (method == PermissionMethod.READ.value) {
                    return true
                }
            }
            return false
        }
        return false
    }
}