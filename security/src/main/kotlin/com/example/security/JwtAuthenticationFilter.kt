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
    authenticationManager: AuthenticationManager?,
    authorizationService: AuthorizationService
) : BasicAuthenticationFilter(authenticationManager) {

    var authorizationService: AuthorizationService

    init {
        this.authorizationService = authorizationService
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authorization = request.getHeader(KEY_AUTHORIZATION)
        if (null != authorization && authorization.startsWith(BEARER_PRE)) {
            val jwt = authorization.substring(BEARER_PRE.length)
            val authorizationToken = getAuthorizationToken(jwt)
            if (null != authorizationToken) {
                SecurityContextHolder.getContext().authentication = authorizationToken
            }
        }
        super.doFilterInternal(request, response, chain)
    }

    private fun getAuthorizationToken(jwt: String): UsernamePasswordAuthenticationToken? {
        val authorization: Authorization = authorizationService.getByJwt(jwt) ?: return null
        return UsernamePasswordAuthenticationToken(
            authorization.user,
            jwt,
            authorization.permissionAuthorities
        )
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