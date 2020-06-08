package com.example.consumer.controller

import com.example.base.Response
import com.example.base.config.*
import com.example.base.okResponse
import com.example.security.getSecurityAuthorization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    fun consumerToProducer(): Response {
        mqService.send(MQ_CONSUMER_TO_PRODUCER, "hello,world!")
        val mqMessage = MqMessage(queue = MQ_CONSUMER_TO_PRODUCER, modelType = "test", modelId = 0, operate = MqMessageOperateEnum.CREATE.value)
        mqService.send(mqMessage)
        return okResponse()
    }

    @GetMapping("consumer_to_elasticsearch/{id}")
    fun consumerToElasticsearch(@PathVariable id: Long): Response {
        mqService.send(MQ_CONSUMER_TO_ELASTICSEARCH, "hello,world!")
        val mqMessage = MqMessage(
                queue = MQ_CONSUMER_TO_ELASTICSEARCH,
                modelType = "consumer",
                modelId = id,
                operate = MqMessageOperateEnum.CREATE.value,
                authorization = getSecurityAuthorization()
        )
        mqService.send(mqMessage)
        return okResponse()
    }
}