package com.example.elasticsearch.security

import com.example.base.JwtAuthorizationRequest
import com.example.base.getResponseData
import com.example.elasticsearch.service.AccountRpcService
import com.example.security.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestControllerAdvice

@Configuration
@EnableWebSecurity
class AuthenticationConfig : BaseAuthenticationConfig() {

    @Autowired
    lateinit var elasticsearchAuthorizationService: AuthorizationService

    override fun getAuthorizationService(): AuthorizationService {
        return elasticsearchAuthorizationService
    }
}

@Service
class AuthorizationServiceImpl : AuthorizationService {

    @Autowired
    lateinit var accountRpcService: AccountRpcService

    override fun getByJwt(jwt: String): Authorization? {
        val request = JwtAuthorizationRequest(jwt, "elasticsearch")
        val response = accountRpcService.authorization(request)
        return getResponseData(response, "authorization", Authorization::class.java)
    }
}

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class PermissionConfig : BasePermissionConfig()

@RestControllerAdvice
class ExceptionHandler : BaseExceptionHandler()