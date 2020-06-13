package com.example.elasticsearch.service

import com.example.elasticsearch.document.ConsumerDocument
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class ConsumerDocumentService : BaseDocumentServiceImpl<ConsumerDocument>() {

    @Autowired
    lateinit var consumerDocumentRepository: ConsumerDocumentRepository

    override fun getDocumentRepository(): BaseDocumentRepository<ConsumerDocument> {
        return consumerDocumentRepository
    }

    override fun getIndex(): String {
        return "consumer"
    }
}

@Repository
interface ConsumerDocumentRepository : BaseDocumentRepository<ConsumerDocument>

data class ConsumerDocumentFilterRequest(
        var name: String = ""
) : DocumentFilterRequest() {

    override fun queryBuilder(): BoolQueryBuilder {
        val queryBuilder = BoolQueryBuilder()
        createTimeStart?.let {
            val subQueryBuilder = QueryBuilders.rangeQuery("consumer.createTime").gt(it.time)
            queryBuilder.must(subQueryBuilder)
        }
        createTimeEnd?.let {
            val subQueryBuilder = QueryBuilders.rangeQuery("consumer.createTime").lt(it.time)
            queryBuilder.must(subQueryBuilder)
        }
        if (name.isNotBlank()) {
            val subQueryBuilder = QueryBuilders.matchQuery("consumer.name", name)
            queryBuilder.must(subQueryBuilder)
        }
        return queryBuilder
    }

    override fun getOrderFieldDirection(): String {
        return "consumer.createTime desc"
    }
}