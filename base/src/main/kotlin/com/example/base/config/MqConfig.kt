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

@Configuration
class MqConfig {

    companion object {
        const val MQ_CONSUMER_TO_PRODUCER = "consumer_to_producer"
        const val MQ_CONSUMER_TO_ELASTICSEARCH = "consumer_to_elasticsearch"
    }

    @Bean
    fun customerToProducer(): Queue {
        return Queue(MQ_CONSUMER_TO_PRODUCER)
    }

    @Bean
    fun customerToElasticsearch(): Queue {
        return Queue(MQ_CONSUMER_TO_ELASTICSEARCH)
    }
}
