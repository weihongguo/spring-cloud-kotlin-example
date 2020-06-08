package com.example.producer.security

import com.example.base.JwtAuthorizationRequest
import com.example.base.getResponseData
import com.example.producer.service.AccountRpcService
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
    lateinit var producerAuthorizationService: AuthorizationService

    override fun getAuthorizationService(): AuthorizationService {
        return producerAuthorizationService
    }
}

@Service
class AuthorizationServiceImpl : AuthorizationService {

    @Autowired
    lateinit var accountRpcService: AccountRpcService

    override fun getByJwt(jwt: String): Authorization? {
        val request = JwtAuthorizationRequest(jwt, "producer")
        val response = accountRpcService.authorization(request)
        return getResponseData(response, "authorization", Authorization::class.java)
    }
}

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class PermissionConfig : BasePermissionConfig()

@RestControllerAdvice
class ExceptionHandler : BaseExceptionHandler()