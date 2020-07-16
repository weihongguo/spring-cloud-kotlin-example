package com.example.producer.mq

import com.example.base.mq.CustomMessage
import com.example.base.mq.EntityMessage
import com.example.base.mq.MqConfig.Companion.MQ_CONSUMER_TO_PRODUCER_CUSTOM
import com.example.base.mq.MqConfig.Companion.MQ_CONSUMER_TO_PRODUCER_ENTITY
import com.example.base.mq.MqLogService
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @Author：GuoGuo
 * @Date 2020/6/4 16:44
 **/

@Component
class ConsumerToProducerEntityReceiver {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val QUEUE = MQ_CONSUMER_TO_PRODUCER_ENTITY
    }

    @Autowired
    lateinit var mqLogService: MqLogService

    @RabbitListener(queues = [QUEUE])
    fun process(channel: Channel, message: Message) {
        channel.basicAck(message.messageProperties.deliveryTag, false);

        try {
            val entityMessage = ObjectMapper().readValue(message.body, EntityMessage::class.java)

            log.info(message.body.toString())
            log.info(entityMessage.toString())

            /* 模拟错误处理 */
            if (entityMessage.entityType != "consumer") {
                val reason = "entity type error [${entityMessage.entityType} != consumer]"
                throw RuntimeException(reason)
            }
            if (entityMessage.entityId <= 0) {
                val reason = "entity id error [${entityMessage.entityId} <= 0]"
                throw RuntimeException(reason)
            }

            log.info("ConsumerToProducerReceiver deal success $entityMessage")
        } catch (e: Exception) {
            val reason = e.message ?: "undefined"
            log.info("\nConsumerToProducerReceiver deal fail $reason\n")
            mqLogService.processFail(QUEUE, String(message.body), reason)
        }
    }
}


@Component
class ConsumerToProducerCustomReceiver {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val QUEUE = MQ_CONSUMER_TO_PRODUCER_CUSTOM
    }

    @Autowired
    lateinit var mqLogService: MqLogService

    @RabbitListener(queues = [QUEUE])
    fun process(channel: Channel, message: Message) {
        channel.basicAck(message.messageProperties.deliveryTag, false);

        try {
            val customMessage = ObjectMapper().readValue(message.body, CustomMessage::class.java)

            log.info("ConsumerToProducerReceiver deal success $customMessage")
        } catch (e: Exception) {
            val reason = e.message ?: "undefined"
            log.info("\nConsumerToProducerReceiver deal fail $reason\n")
            mqLogService.processFail(QUEUE, String(message.body), reason)
        }
    }
}