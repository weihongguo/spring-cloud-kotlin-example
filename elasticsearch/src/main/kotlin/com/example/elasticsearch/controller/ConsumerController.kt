package com.example.elasticsearch.controller

import com.example.base.Response
import com.example.base.okResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("consumer")
class ConsumerController {

    @GetMapping("{id}")
    fun show(@PathVariable id: Long): Response {
        return okResponse()
    }
}