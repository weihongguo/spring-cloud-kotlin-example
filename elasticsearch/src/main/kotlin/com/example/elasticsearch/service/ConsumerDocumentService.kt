package com.example.elasticsearch.service

import com.example.elasticsearch.document.ConsumerDocument.Companion.CONSUMER_INDEX_NAME
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.rest.RestStatus
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.stereotype.Service

@Service
class ConsumerDocumentService : DocumentService() {

    fun nameCountMap(): Map<String, Long>? {
        val sourceBuilder = SearchSourceBuilder().size(0)
        sourceBuilder.aggregation(AggregationBuilders.terms("nameCount").field("consumer.name.keyword"))
        val searchRequest = SearchRequest(CONSUMER_INDEX_NAME).source(sourceBuilder)
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