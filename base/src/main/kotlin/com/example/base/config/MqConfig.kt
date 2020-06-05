package com.example.base.config

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/6/4 16:10
 **/

const val MQ_CONSUMER_TO_PRODUCER = "consumer_to_producer"

@Configuration
class MqConfig {

    @Bean
    fun customerToProducer(): Queue {
        return Queue(MQ_CONSUMER_TO_PRODUCER)
    }
}

class MqSender(private var rabbitTemplate: RabbitTemplate) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun send(routingKey: String, message: String) {
        log.info("send $routingKey $message")
        rabbitTemplate.convertAndSend(routingKey, message)
    }
}

class RabbitCallback : RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private val log = LoggerFactory.getLogger(javaClass)

    init {
        print("####### RabbitCallback init #######")
    }

    override fun confirm(correlationData: CorrelationData?, ack: Boolean, cause: String?) {
        print("####### RabbitCallback confirm #######")
        log.info("### confirm $ack $cause $correlationData ###")
    }

    override fun returnedMessage(message: Message, replyCode: Int, replyText: String, exchange: String, routingKey: String) {
        print("####### RabbitCallback returnedMessage #######")
        log.info("### returnedMessage $message $replyCode $replyText $exchange $routingKey ###")
    }
}

@Service
class MqService(private final var rabbitTemplate: RabbitTemplate) {

    init {
        print("####### MqService init #######")
        val rabbitCallback = RabbitCallback()
        rabbitTemplate.setReturnCallback(rabbitCallback)
        rabbitTemplate.setConfirmCallback(rabbitCallback)
        print("\n\n****##### MqService $rabbitTemplate #######*****\n\n")
    }

    fun send(queue: String, message: String) {
        print("\n\n****##### MqService $rabbitTemplate #######*****\n\n")
        rabbitTemplate.convertAndSend(queue, message)
    }

    fun sendJson(queue: String, message: Any) {
        rabbitTemplate.convertAndSend(queue, JSON.toJSONString(message))
    }
}