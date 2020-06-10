package com.example.account.controller

import com.example.account.request.LoginRequest
import com.example.account.security.JwtConfig
import com.example.account.security.JwtUser
import com.example.account.security.generateJwt
import com.example.account.service.UserService
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.model.ModelNotFoundException
import com.example.base.okResponse
import com.example.security.AuthorizationUserType
import com.example.security.matchesPassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtConfig: JwtConfig

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): Response {
        if (!request.check()) {
            throw RequestException()
        }
        val user = userService.getByMobile(request.mobile) ?: throw ModelNotFoundException("用户不存在")
        if (!matchesPassword(request.password, user.password)) {
            throw RuntimeException("密码错误")
        }
        val jwtUser = JwtUser(AuthorizationUserType.USER.value, user.id!!)
        val authToken = generateJwt(jwtConfig, jwtUser)
        return okResponse(mapOf(
            "authToken" to authToken
        ))
    }
}