package com.example.account.controller

import com.example.account.request.RegisterRequest
import com.example.account.security.JwtConfig
import com.example.account.security.JwtUser
import com.example.account.security.generateJwt
import com.example.account.service.UserService
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.model.ModelExistedException
import com.example.base.model.User
import com.example.base.okResponse
import com.example.security.AuthorizationUserType
import com.example.security.encodePassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterController {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtConfig: JwtConfig

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequest): Response {
        if (!request.check()) {
            throw RequestException()
        }
        userService.getByMobile(request.mobile)?.let {
            throw ModelExistedException("用户已经存在")
        }
        val user = User(mobile = request.mobile, password = encodePassword(request.password))
        val saveUser = userService.save(user)
        val jwtUser = JwtUser(AuthorizationUserType.USER.value, saveUser.id!!)
        val authToken = generateJwt(jwtConfig, jwtUser)
        return okResponse(mapOf(
            "authToken" to authToken
        ))
    }
}