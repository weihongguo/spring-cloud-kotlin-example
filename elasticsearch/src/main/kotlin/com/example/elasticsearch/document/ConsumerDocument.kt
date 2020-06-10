package com.example.elasticsearch.document

import com.example.base.consumer.Consumer
import com.example.base.producer.Producer
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "consumer", type = "_type")
data class ConsumerDocument (
        var consumer: Consumer,
        var producer: Producer
) : BaseDocument() {
    init {
        this.id = consumer.id.toString()
    }
}
