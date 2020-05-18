package com.example.account.controller

import com.example.account.request.RegisterRequest
import com.example.account.security.JwtConfig
import com.example.account.security.JwtUser
import com.example.account.security.generateJwt
import com.example.account.service.UserService
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import com.example.database.entity.EntityExistedException
import com.example.database.entity.User
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
            throw EntityExistedException("用户已经存在")
        }

        val password = encodePassword(request.password)
        val user = User(mobile = request.mobile, password = password)
        userService.save(user)

        var jwtUser = JwtUser(AuthorizationUserType.USER.value, user.id!!)
        var authorizationJwt = generateJwt(jwtConfig, jwtUser)
        return okResponse(mapOf(
            "authorizationJwt" to authorizationJwt
        ))
    }
}