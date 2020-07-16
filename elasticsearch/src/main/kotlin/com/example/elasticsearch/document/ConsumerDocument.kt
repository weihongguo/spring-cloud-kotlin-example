package com.example.elasticsearch.document

import com.example.base.consumer.Consumer
import com.example.base.producer.Producer
import com.example.elasticsearch.service.DocumentFilterRequest
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders

data class ConsumerDocument (
    var consumer: Consumer? = null,
    var producer: Producer? = null
) : BaseDocument() {
    companion object {
        const val CONSUMER_INDEX_NAME = "consumer"
    }

    init {
        this.id = consumer?.id.toString()
    }

    override fun index(): String {
        return CONSUMER_INDEX_NAME
    }
}

data class ConsumerDocumentFilterRequest(
    var name: String = ""
) : DocumentFilterRequest() {

    override fun queryBuilder(): BoolQueryBuilder {
        val boolQueryBuilder = QueryBuilders.boolQuery()
        createTimeStart?.let {
            val subQueryBuilder = QueryBuilders.rangeQuery("consumer.createTime").gt(it.time)
            boolQueryBuilder.must(subQueryBuilder)
        }
        createTimeEnd?.let {
            val subQueryBuilder = QueryBuilders.rangeQuery("consumer.createTime").lt(it.time)
            boolQueryBuilder.must(subQueryBuilder)
        }
        if (name.isNotBlank()) {
            val subQueryBuilder = QueryBuilders.matchQuery("consumer.name", name)
            boolQueryBuilder.must(subQueryBuilder)
        }
        return boolQueryBuilder
    }

    override fun getDefaultFieldDirection(): String {
        return "consumer.createTime desc"
    }
}
