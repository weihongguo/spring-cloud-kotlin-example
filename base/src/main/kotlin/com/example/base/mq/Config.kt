package com.example.base.mq

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
