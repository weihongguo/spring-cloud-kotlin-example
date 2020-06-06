package com.example.consumer.controller

import com.example.base.Response
import com.example.base.config.MQ_CONSUMER_TO_PRODUCER
import com.example.base.config.MqMessage
import com.example.base.config.MqMessageOperateEnum
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

    @GetMapping("consumer_to_producer")
    fun send(): Response {
        mqService.send(MQ_CONSUMER_TO_PRODUCER, "hello,world!")
        val mqMessage = MqMessage(queue = MQ_CONSUMER_TO_PRODUCER, modelType = "test", modelId = 0, operate = MqMessageOperateEnum.CREATE.value)
        mqService.send(mqMessage)
        return okResponse()
    }
}