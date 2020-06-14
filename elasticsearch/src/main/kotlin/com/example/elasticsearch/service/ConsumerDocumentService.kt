package com.example.elasticsearch.service

import com.example.elasticsearch.document.ConsumerDocument
import com.example.elasticsearch.document.ConsumerDocument.Companion.INDEX_NAME
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.rest.RestStatus
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.elasticsearch.search.builder.SearchSourceBuilder
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
        return INDEX_NAME
    }

    fun nameCountMap(): Map<String, Long>? {
        val sourceBuilder = SearchSourceBuilder().size(0)
        sourceBuilder.aggregation(AggregationBuilders.terms("nameCount").field("consumer.name.keyword"))
        val searchRequest = SearchRequest(INDEX_NAME).source(sourceBuilder)
        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        if (searchResponse.status() == RestStatus.OK) {
            val aggregations = searchResponse.aggregations
            val nameCountTerm = aggregations.get<ParsedStringTerms>("nameCount")
            val nameCountMap = mutableMapOf<String, Long>()
            nameCountTerm.buckets.forEach {
                nameCountMap[it.key as String] = it.docCount
            }
            return nameCountMap
        }
        return null
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

    override fun getDefaultFieldDirection(): String {
        return "consumer.createTime desc"
    }
}