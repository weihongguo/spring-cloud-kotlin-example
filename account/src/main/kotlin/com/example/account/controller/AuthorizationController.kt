package com.example.account.controller

import com.example.base.response.Response
import com.example.base.response.responseOk
import com.example.security.AuthorizationService
import com.example.security.getContextAuthorizationJwt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorizationController {
    @Autowired
    lateinit var accountAuthorizationService: AuthorizationService

    @GetMapping("authorization")
    fun authorization(): Response {
        val authorizationJwt = getContextAuthorizationJwt() ?: throw RuntimeException("请先登录")
        val authorization = accountAuthorizationService.getByJwt(authorizationJwt) ?: throw RuntimeException("登录态错误")
        var response = responseOk()
        response.data = mapOf("authorization" to authorization)
        return response
    }
}