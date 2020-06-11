package com.example.producer.mq

import com.alibaba.fastjson.JSON
import com.example.base.mq.EntityMessage
import com.example.base.mq.MqConfig.Companion.MQ_CONSUMER_TO_PRODUCER
import com.example.base.mq.MqLogService
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
class ConsumerToProducerReceiver {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val QUEUE = MQ_CONSUMER_TO_PRODUCER
    }

    @Autowired
    lateinit var mqLogService: MqLogService

    @RabbitListener(queues = [QUEUE])
    fun process(channel: Channel, message: Message) {
        channel.basicAck(message.messageProperties.deliveryTag, false);

        try {
            val entityMessage = JSON.parseObject<EntityMessage>(message.body, EntityMessage::class.java)

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
            mqLogService.processFail(QUEUE, String(message.body), e.toString())
        }
    }
}