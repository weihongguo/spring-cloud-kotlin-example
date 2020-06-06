package com.example.producer.mq

import com.alibaba.fastjson.JSON
import com.example.base.config.MQ_CONSUMER_TO_PRODUCER
import com.example.base.config.MqMessage
import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

/**
 * @Author：GuoGuo
 * @Date 2020/6/4 16:44
 **/

@Component
class ConsumerToProducerReceiver {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = [MQ_CONSUMER_TO_PRODUCER])
    fun process(channel: Channel, message: Message) {
        channel.basicAck(message.messageProperties.deliveryTag, false);

        val mqMessage = JSON.parseObject<MqMessage>(message.body, MqMessage::class.java)
        log.info("receive ${mqMessage.toString()}")
    }
}