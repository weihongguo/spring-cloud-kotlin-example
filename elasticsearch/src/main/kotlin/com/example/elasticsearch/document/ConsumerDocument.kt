package com.example.elasticsearch.document

import com.example.base.consumer.Consumer
import com.example.base.producer.Producer
import com.example.elasticsearch.document.ConsumerDocument.Companion.INDEX_NAME
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = INDEX_NAME)
data class ConsumerDocument (
        var consumer: Consumer,
        var producer: Producer
) : BaseDocument() {
    companion object {
        const val INDEX_NAME = "consumer"
    }
    init {
        this.id = consumer.id.toString()
    }
}
