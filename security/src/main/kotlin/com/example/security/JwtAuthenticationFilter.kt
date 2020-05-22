package com.example.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
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
                val jwt = authorization.substring(BEARER_PRE.length)
                getAuthorizationToken(jwt)?.let {
                    SecurityContextHolder.getContext().authentication = it
                }
            }
        }
        super.doFilterInternal(request, response, chain)
    }

    private fun getAuthorizationToken(jwt: String): UsernamePasswordAuthenticationToken? {
        authorizationService.getByJwt(jwt)?.let {
            return UsernamePasswordAuthenticationToken(
                it.user,
                jwt,
                it.permissionAuthorities
            )
        }
        return null
    }
}

fun getContextAuthorizationUser(): AuthorizationUser? {
    return when(val principal = SecurityContextHolder.getContext().authentication.principal) {
        is AuthorizationUser -> principal
        else -> null
    }
}

fun getContextAuthorizationUserType(): String? {
    return getContextAuthorizationUser()?.type
}

fun getContextAuthorizationUserId(): Long? {
    return getContextAuthorizationUser()?.id
}

fun getContextAuthorizationJwt(): String? {
    return when(val credentials = SecurityContextHolder.getContext().authentication.credentials) {
        is String -> credentials
        else -> null
    }
}