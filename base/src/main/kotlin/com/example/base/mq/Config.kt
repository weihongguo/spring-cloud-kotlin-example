package com.example.base.mq

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MqConfig {

    companion object {
        const val MQ_CONSUMER_TO_PRODUCER_ENTITY = "consumer_to_producer_entity"
        const val MQ_CONSUMER_TO_PRODUCER_CUSTOM = "consumer_to_producer_custom"
        const val MQ_CONSUMER_TO_ELASTICSEARCH = "consumer_to_elasticsearch"
    }

    @Bean
    fun customerToProducerEntity(): Queue {
        return Queue(MQ_CONSUMER_TO_PRODUCER_ENTITY)
    }

    @Bean
    fun customerToProducerEntityCustom(): Queue {
        return Queue(MQ_CONSUMER_TO_PRODUCER_CUSTOM)
    }

    @Bean
    fun customerToElasticsearch(): Queue {
        return Queue(MQ_CONSUMER_TO_ELASTICSEARCH)
    }
}
