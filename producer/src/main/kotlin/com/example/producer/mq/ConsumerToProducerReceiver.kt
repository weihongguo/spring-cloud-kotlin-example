package com.example.producer.mq

import com.example.base.config.MQ_CONSUMER_TO_PRODUCER
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
        log.info("receive ${String(message.body)}")
    }
}