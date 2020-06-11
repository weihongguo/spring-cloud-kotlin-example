package com.example.base.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Authorization (
    var user: AuthorizationUser? = null,
    var permissionAuthorities: Collection<PermissionAuthority>? = null
)

class AuthorizationUser (
    var type: String = "",
    var id: Long = 0L,
    var nickname: String = "",
    var avatarUrl: String = ""
)

enum class AuthorizationUserType(var value: String, var label: String) {
    USER("user", "用户")
}

private const val KEY_AUTHORIZATION = "Authorization"
private const val BEARER_PRE = "Bearer "

interface AuthorizationService {
    fun getByJwt(jwt: String): Authorization?
}

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
    return getSecurityAuthorization()?.substring(BEARER_PRE.length)
}