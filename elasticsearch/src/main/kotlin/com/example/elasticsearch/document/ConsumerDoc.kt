package com.example.elasticsearch.document

import com.example.database.entity.Consumer
import com.example.database.entity.Producer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Service

@Document(indexName = "consumer", type = "_type")
data class ConsumerDoc (
    var consumer: Consumer,
    var producer: Producer
) : BaseDoc() {
    init {
        this.id = consumer.id.toString()
    }
}
