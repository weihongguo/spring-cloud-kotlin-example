package com.example.producer.security

import com.example.base.JwtAuthorizationRequest
import com.example.base.getResponseData
import com.example.producer.service.AccountService
import com.example.security.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.stereotype.Service

@Configuration
@EnableWebSecurity
class ProducerAuthenticationConfig : BaseAuthenticationConfig() {

    @Autowired
    lateinit var producerAuthorizationService: AuthorizationService

    override fun getAuthorizationService(): AuthorizationService {
        return producerAuthorizationService
    }
}


@Service
class ProducerAuthorizationServiceImpl : AuthorizationService {

    @Autowired
    lateinit var accountService: AccountService

    override fun getByJwt(jwt: String): Authorization? {
        val request = JwtAuthorizationRequest(jwt, "producer")
        val response = accountService.authorization(request)
        return getResponseData(response, "authorization", Authorization::class.java)
    }
}