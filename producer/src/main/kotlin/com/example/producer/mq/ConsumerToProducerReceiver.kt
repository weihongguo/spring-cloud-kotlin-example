package com.example.producer.mq

import com.alibaba.fastjson.JSON
import com.example.base.config.MqConfig.Companion.MQ_CONSUMER_TO_PRODUCER
import com.example.database.entity.MqFailLog
import com.example.database.entity.MqFailLogOperateEnum
import com.example.database.service.EntityMessage
import com.example.database.service.MqFailLogService
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
    companion object {
        const val QUEUE = MQ_CONSUMER_TO_PRODUCER
    }

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var mqFailLogService: MqFailLogService

    @RabbitListener(queues = [QUEUE])
    fun process(channel: Channel, message: Message) {
        channel.basicAck(message.messageProperties.deliveryTag, false);

        try {
            val entityMessage = JSON.parseObject<EntityMessage>(message.body, EntityMessage::class.java)
            log.info("receive $entityMessage")
            /* 模拟错误处理 */
            if (entityMessage.entityType != "producer") {
                val reason = "entity type error"
                val mqFailLog = MqFailLog(
                    queue = QUEUE,
                    message = String(message.body),
                    operate = MqFailLogOperateEnum.PROCESS.value,
                    reason = reason
                )
                mqFailLogService.save(mqFailLog)
                return
            }

            if (entityMessage.entityId <= 0) {
                val reason = "entity id error"
                val mqFailLog = MqFailLog(
                    queue = QUEUE,
                    message = String(message.body),
                    operate = MqFailLogOperateEnum.PROCESS.value,
                    reason = reason
                )
                mqFailLogService.save(mqFailLog)
                return
            }
        } catch (e: Exception) {
            val reason = e.toString()
            val mqFailLog = MqFailLog(
                queue = QUEUE,
                message = String(message.body),
                operate = MqFailLogOperateEnum.PROCESS.value,
                reason = reason
            )
            mqFailLogService.save(mqFailLog)
        }
    }
}