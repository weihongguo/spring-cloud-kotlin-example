package com.example.account.controller

import com.example.account.request.PasswordRequest
import com.example.account.service.UserService
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import com.example.database.entity.EntityNotFoundException
import com.example.security.encodePassword
import com.example.security.getContextAuthorizationUser
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
    fun update(@RequestBody request: PasswordRequest): Response? {
        if (!request.check()) {
            throw RequestException()
        }
        val authorizationUser = getContextAuthorizationUser() ?: throw RuntimeException()
        val user = userService.getById(authorizationUser.id) ?: throw EntityNotFoundException()
        if (!matchesPassword(request.old, user.password)) {
            throw RuntimeException("密码错误")
        }
        user.password = encodePassword(request.new)
        userService.updateDirect(user)
        return okResponse()
    }
}