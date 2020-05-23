package com.example.account.controller

import com.example.account.request.InitRequest
import com.example.account.service.InitService
import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InitController {

    @Autowired
    lateinit var initService: InitService

    @PostMapping("init")
    fun init(@RequestBody request: InitRequest): Response {
        if (!request.check()) {
            throw RequestException()
        }
        initService.init(request)
        return okResponse()
    }
}