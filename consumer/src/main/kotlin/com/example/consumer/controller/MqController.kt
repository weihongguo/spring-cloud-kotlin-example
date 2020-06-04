package com.example.consumer.controller

import com.example.base.Response
import com.example.base.config.MQ_CONSUMER_TO_PRODUCER
import com.example.base.config.MqService
import com.example.base.okResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author：GuoGuo
 * @Date 2020/6/4 16:40
 **/
@RestController
@RequestMapping("mq")
class MqController {

    @Autowired
    lateinit var mqService: MqService

    @GetMapping("test")
    fun send(): Response {
        mqService.send(MQ_CONSUMER_TO_PRODUCER, "hello,world!")
        return okResponse()
    }
}