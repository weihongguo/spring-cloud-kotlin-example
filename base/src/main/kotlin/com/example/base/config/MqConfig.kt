package com.example.base.config

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/6/4 16:10
 **/

const val MQ_CONSUMER_TO_PRODUCER = "consumer_to_producer"

@Configuration
class MqConfig {

    @Bean
    fun customerToProducer() : Queue {
        return Queue(MQ_CONSUMER_TO_PRODUCER)
    }
}

@Component
final class MqSender(private val rabbitTemplate: RabbitTemplate) : RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private val log = LoggerFactory.getLogger(javaClass)

    init {
        rabbitTemplate.setConfirmCallback(this)
        rabbitTemplate.setReturnCallback(this)
    }

    fun send(routingKey: String, message: String) {
        log.info("send $routingKey $message")
        rabbitTemplate.convertAndSend(routingKey, message)
    }

    override fun confirm(correlationData: CorrelationData?, ack: Boolean, cause: String?) {
        log.info("### confirm $ack $cause $correlationData ###")
    }

    override fun returnedMessage(message: Message, replyCode: Int, replyText: String, exchange: String, routingKey: String) {
        log.info("### returnedMessage $message $replyCode $replyText $exchange $routingKey ###")
    }
}

@Service
class MqService {

    @Autowired
    lateinit var mqSender: MqSender

    fun send(queue: String, message: String) {
        mqSender.send(queue, message)
    }

    fun sendJson(queue: String, message: Any) {
        mqSender.send(queue, JSON.toJSONString(message))
    }
}

