package com.example.account.controller

import com.example.account.request.PasswordUpdateRequest
import com.example.account.service.UserService
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.model.ModelNotFoundException
import com.example.base.okResponse
import com.example.security.encodePassword
import com.example.security.getSecurityAuthorizationUser
import com.example.security.matchesPassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PasswordController {

    @Autowired
    lateinit var userService: UserService

    @PutMapping(value = ["password"])
    fun update(@RequestBody request: PasswordUpdateRequest): Response? {
        if (!request.check()) {
            throw RequestException()
        }
        val authorizationUser = getSecurityAuthorizationUser() ?: throw RuntimeException()
        val user = userService.getById(authorizationUser.id) ?: throw ModelNotFoundException()
        if (!matchesPassword(request.old, user.password)) {
            throw RuntimeException("密码错误")
        }
        user.password = encodePassword(request.new)
        userService.update(user, true)
        return okResponse()
    }
}