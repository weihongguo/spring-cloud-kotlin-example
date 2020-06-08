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
const val MQ_CONSUMER_TO_ELASTICSEARCH = "consumer_to_elasticsearch"

@Configuration
class MqConfig {

    @Bean
    fun customerToProducer(): Queue {
        return Queue(MQ_CONSUMER_TO_PRODUCER)
    }

    @Bean
    fun customerToElasticsearch(): Queue {
        return Queue(MQ_CONSUMER_TO_ELASTICSEARCH)
    }
}

class MqMessage(
        var queue: String,
        var message: String = "",
        var modelType: String = "",
        var modelId: Long = 0,
        var operate: String = "",
        var authorization: String? = null
) {
    override fun toString(): String {
        return "queue: \"$queue\"; message: \"$message\"; modelType: \"$modelType\"; modelId: $modelId, operate: \"$operate\"; authorization: \"$authorization\""
    }
}

enum class MqMessageOperateEnum (var value: String, var label: String) {
    CREATE("create", "创建"),
    UPDATE("update", "更新"),
    DELETE("delete", "删除")
}

class RabbitCallback : RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private val log = LoggerFactory.getLogger(javaClass)

    init {
        print("####### RabbitCallback init #######")
    }

    override fun confirm(correlationData: CorrelationData?, ack: Boolean, cause: String?) {
        log.info("### confirm $ack $cause $correlationData ###")
    }

    override fun returnedMessage(message: Message, replyCode: Int, replyText: String, exchange: String, routingKey: String) {
        log.info("### returnedMessage $message $replyCode $replyText $exchange $routingKey ###")
    }
}

@Service
class MqService(private final var rabbitTemplate: RabbitTemplate) {

    init {
        print("####### MqService init #######")
        val rabbitCallback = RabbitCallback()
        rabbitTemplate.setConfirmCallback(rabbitCallback)
        rabbitTemplate.setReturnCallback(rabbitCallback)
        print("\n\n****##### MqService $rabbitTemplate #######*****\n\n")
    }

    fun send(queue: String, message: String, token: String = "") {
        val mqMessage = MqMessage(queue = queue, message = message)
        rabbitTemplate.convertAndSend(queue, JSON.toJSONString(mqMessage))
    }

    fun send(message: MqMessage) {
        rabbitTemplate.convertAndSend(message.queue, JSON.toJSONString(message))
    }
}