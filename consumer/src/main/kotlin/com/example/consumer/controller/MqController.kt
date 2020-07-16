package com.example.consumer.controller

import com.example.base.Response
import com.example.base.mq.CustomMessage
import com.example.base.mq.EntityMessage
import com.example.base.mq.EntityMessageOperateEnum
import com.example.base.mq.MqConfig.Companion.MQ_CONSUMER_TO_ELASTICSEARCH
import com.example.base.mq.MqConfig.Companion.MQ_CONSUMER_TO_PRODUCER_CUSTOM
import com.example.base.mq.MqConfig.Companion.MQ_CONSUMER_TO_PRODUCER_ENTITY
import com.example.base.mq.MqService
import com.example.base.okResponse
import com.example.base.security.getSecurityAuthorization
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
        val entityMessage = EntityMessage(
            queue = MQ_CONSUMER_TO_PRODUCER_ENTITY,
            entityType = "consumer",
            entityId = 1,
            operate = EntityMessageOperateEnum.CREATE.value
        )
        mqService.send(entityMessage)

        val customMessage = CustomMessage(
            queue = MQ_CONSUMER_TO_PRODUCER_CUSTOM,
            content = "hello, world!"
        )
        mqService.send(customMessage)

        return okResponse()
    }

    @GetMapping("consumer_to_elasticsearch/{id}")
    fun consumerToElasticsearch(@PathVariable id: Long): Response {
        val mqMessage = EntityMessage(
            queue = MQ_CONSUMER_TO_ELASTICSEARCH,
            entityType = "consumer",
            entityId = id,
            operate = EntityMessageOperateEnum.CREATE.value,
            authorization = getSecurityAuthorization()
        )
        mqService.send(mqMessage)
        return okResponse()
    }
}