package com.example.consumer.controller

import com.example.base.Response
import com.example.base.getResponseData
import com.example.base.okResponse
import com.example.consumer.service.TestService
import com.example.database.entity.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 18:12
 **/
@RestController
@RequestMapping("test")
class TestController {

    @Autowired
    lateinit var testService: TestService

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/consumer/test/{id}', 'READ')")
    fun show(@PathVariable id: Long): Response {
        val rpcResponse = testService.show(id)
        val test = getResponseData(rpcResponse, "test", Test::class.java)
        return okResponse(mapOf(
            "test" to test
        ))
    }
}