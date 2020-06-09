package com.example.elasticsearch.mq

import com.alibaba.fastjson.JSON
import com.example.base.config.MqConfig.Companion.MQ_CONSUMER_TO_ELASTICSEARCH
import com.example.base.getResponseData
import com.example.database.entity.Consumer
import com.example.database.entity.Producer
import com.example.database.service.EntityMessage
import com.example.elasticsearch.document.ConsumerDocument
import com.example.elasticsearch.service.ConsumerDocumentRepository
import com.example.elasticsearch.service.ConsumerRpcService
import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ConsumerToElasticsearchReceiver {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var consumerRpcService: ConsumerRpcService
    @Autowired
    lateinit var consumerDocumentRepository: ConsumerDocumentRepository

    @RabbitListener(queues = [MQ_CONSUMER_TO_ELASTICSEARCH])
    fun process(channel: Channel, message: Message) {
        channel.basicAck(message.messageProperties.deliveryTag, false);

        val entityMessage = JSON.parseObject<EntityMessage>(message.body, EntityMessage::class.java)
        log.info("receive $entityMessage")

        entityMessage.authorization?.let {
            if (entityMessage.entityType == "consumer" && entityMessage.entityId > 0) {
                val response = consumerRpcService.consumerShow(entityMessage.entityId, it)
                log.info("rpc receive $response")
                val consumer = getResponseData(response, "consumer", Consumer::class.java)
                val producer = getResponseData(response, "producer", Producer::class.java)
                if (consumer != null && producer != null) {
                    val consumerDocument = ConsumerDocument(consumer = consumer, producer = producer)
                    consumerDocumentRepository.save(consumerDocument)
                    log.info("save $consumerDocument")
                }
            }
        }
    }
}