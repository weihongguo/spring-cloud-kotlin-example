package com.example.account.controller

import com.example.account.security.AccountAuthorizationService
import com.example.base.JwtAuthorizationRequest
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import com.example.base.security.getSecurityAuthorizationToken
import com.example.base.security.getSecurityAuthorizationUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorizationController {

    @Autowired
    lateinit var accountAuthorizationService: AccountAuthorizationService

    @GetMapping("auth_info")
    fun authInfo(): Response {
        val authorizationUser = getSecurityAuthorizationUser() ?: throw RuntimeException("请先登录")
        return okResponse(mapOf(
            "authInfo" to authorizationUser
        ))
    }

    @GetMapping("authorization")
    fun authorization(): Response {
        val authorizationJwt = getSecurityAuthorizationToken() ?: throw RuntimeException("请先登录")
        val authorization = accountAuthorizationService.getByJwt(authorizationJwt) ?: throw RuntimeException("登录态错误")
        return okResponse(mapOf(
            "authorization" to authorization
        ))
    }

    @PostMapping("authorization")
    fun authorization(@RequestBody request: JwtAuthorizationRequest): Response {
        if (!request.check()) {
            throw RequestException()
        }
        val authorizationJwt = getSecurityAuthorizationToken() ?: throw RuntimeException("请先登录")
        if (request.jwt != authorizationJwt) {
            throw RuntimeException("请求参数错误")
        }
        val authorization = accountAuthorizationService.getByJwt(authorizationJwt, request.module) ?: throw RuntimeException("登录态错误")
        return okResponse(mapOf(
            "authorization" to authorization
        ))
    }
}