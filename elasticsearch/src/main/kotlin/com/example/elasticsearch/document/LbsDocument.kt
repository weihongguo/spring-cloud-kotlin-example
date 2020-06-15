package com.example.elasticsearch.document

import com.example.elasticsearch.service.DocumentFilterRequest
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders

/**
 * @Author：GuoGuo
 * @Date 2020/6/15 17:25
 **/

data class LbsDocument(
    var name: String,
    var digest: String,
    var location: GeoPoint
) : BaseDocument() {
    companion object {
        const val LBS_INDEX_NAME = "lbs"
    }

    init {
        this.id = location.toString()
    }

    override fun index(): String {
        return LBS_INDEX_NAME
    }
}

data class LbsDocumentFilterRequest(
    var name: String = "",
    var digest: String = ""
) : DocumentFilterRequest() {

    override fun queryBuilder(): BoolQueryBuilder {
        val boolQueryBuilder = QueryBuilders.boolQuery()
        createTimeStart?.let {
            val subQueryBuilder = QueryBuilders.rangeQuery("createTime").gt(it.time)
            boolQueryBuilder.must(subQueryBuilder)
        }
        createTimeEnd?.let {
            val subQueryBuilder = QueryBuilders.rangeQuery("createTime").lt(it.time)
            boolQueryBuilder.must(subQueryBuilder)
        }
        if (name.isNotBlank()) {
            val subQueryBuilder = QueryBuilders.termQuery("name.keyword", name)
            boolQueryBuilder.must(subQueryBuilder)
        }
        if (digest.isNotBlank()) {
            val subQueryBuilder = QueryBuilders.termQuery("digest", digest)
            boolQueryBuilder.must(subQueryBuilder)
        }
        return boolQueryBuilder
    }

    override fun getDefaultFieldDirection(): String {
        return "consumer.createTime desc"
    }
}