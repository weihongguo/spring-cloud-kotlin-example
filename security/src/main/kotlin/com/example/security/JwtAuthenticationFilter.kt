package com.example.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val KEY_AUTHORIZATION = "Authorization"
const val BEARER_PRE = "Bearer "

class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val authorizationService: AuthorizationService
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader(KEY_AUTHORIZATION)?.let { authorization ->
            if (authorization.isNotBlank() && authorization.startsWith(BEARER_PRE)) {
                getAuthorizationToken(authorization)?.let {
                    SecurityContextHolder.getContext().authentication = it
                }
            }
        }
        super.doFilterInternal(request, response, chain)
    }

    private fun getAuthorizationToken(authorization: String): UsernamePasswordAuthenticationToken? {
        val jwt = authorization.substring(BEARER_PRE.length)
        authorizationService.getByJwt(jwt)?.let {
            return UsernamePasswordAuthenticationToken(
                it.user,
                authorization,
                it.permissionAuthorities
            )
        }
        return null
    }
}

fun getSecurityAuthorizationUser(): AuthorizationUser? {
    return when(val principal = SecurityContextHolder.getContext().authentication.principal) {
        is AuthorizationUser -> principal
        else -> null
    }
}

fun getSecurityAuthorizationUserType(): String? {
    return getSecurityAuthorizationUser()?.type
}

fun getSecurityAuthorizationUserId(): Long? {
    return getSecurityAuthorizationUser()?.id
}

fun getSecurityAuthorization(): String? {
    return when(val credentials = SecurityContextHolder.getContext().authentication.credentials) {
        is String -> credentials
        else -> null
    }
}

fun getSecurityAuthorizationToken(): String? {
    getSecurityAuthorization()?.let {
        return it.substring(BEARER_PRE.length)
    }
    return null
}