package com.example.account.controller

import com.example.account.request.LoginRequest
import com.example.account.security.JwtConfig
import com.example.account.security.JwtUser
import com.example.account.security.generateJwt
import com.example.account.service.UserService
import com.example.base.response.Response
import com.example.base.response.responseOk
import com.example.security.AuthorizationUserType
import com.example.security.matchesPassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * @Author：GuoGuo
 * @Date 2020/5/7 16:22
 **/
@RestController
class LoginController {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtConfig: JwtConfig

    @PostMapping("login")
    fun login(@RequestBody request: LoginRequest): Response {
        request.check()
        val user = userService.getByMobile(request.mobile) ?: throw RuntimeException("用户不存在")
        if (!matchesPassword(request.password, user.password)) {
            throw RuntimeException("密码错误")
        }
        var jwtUser = JwtUser(AuthorizationUserType.USER.value, user.id)
        return responseOk(mapOf(
            "token" to generateJwt(jwtConfig, jwtUser)
        ))
    }
}