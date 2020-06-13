package com.example.elasticsearch.service

import com.alibaba.fastjson.JSON
import com.example.base.Pagination
import com.example.base.Response
import com.example.base.errorResponse
import com.example.base.okResponse
import com.example.elasticsearch.document.BaseDocument
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.rest.RestStatus
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*


interface BaseDocumentService<T: BaseDocument> {
    fun getDocumentRepository(): BaseDocumentRepository<T>

    fun getIndex(): String

    fun save(document: T): T?
    fun saveAll(documents: Iterable<T>): Iterable<T>

    fun getById(id: String): T?
    fun page(filterRequest: DocumentFilterRequest) : SearchResponse
}

abstract class BaseDocumentServiceImpl<T: BaseDocument> : BaseDocumentService<T> {

    @Autowired
    lateinit var client: RestHighLevelClient

    override fun getIndex(): String {
        TODO("Not yet implemented")
    }

    override fun save(document: T): T? {
        return getDocumentRepository().save(document)
    }

    override fun saveAll(documents: Iterable<T>): Iterable<T> {
        return getDocumentRepository().saveAll(documents)
    }

    override fun getById(id: String): T? {
        return getDocumentRepository().findById(id).orElse(null)
    }

    override fun page(request: DocumentFilterRequest) : SearchResponse {
        val sourceBuilder = SearchSourceBuilder()
        sourceBuilder.from(request.pageIndex - 1)
                .size(request.pageSize)
                .sort(request.sort())
                .query(request.queryBuilder())
        val searchRequest = SearchRequest(getIndex()).source(sourceBuilder)
        return client.search(searchRequest, RequestOptions.DEFAULT)
    }
}

@NoRepositoryBean
interface BaseDocumentRepository<T: BaseDocument> : ElasticsearchRepository<T, String>

fun <T: BaseDocument> documentPageResponse(request: DocumentFilterRequest, response: SearchResponse, clazz: Class<T>, key: String? = null, extraData: Map<String, *>? = null): Response {
    if (response.status() == RestStatus.OK) {
        val hits = response.hits
        val totalElements = hits.totalHits?.value ?: 0
        var totalPages = totalElements / request.pageSize
        if ((totalElements % request.pageSize) > 0) {
            totalPages++
        }
        val pagination = Pagination(pageIndex = request.pageIndex, pageSize = request.pageSize, totalElements = totalElements, totalPages = totalPages.toInt())
        val list: MutableList<T> = mutableListOf()
        hits.forEach {
            list.add(JSON.parseObject(it.sourceAsString, clazz))
        }
        val pageData = mapOf(
                (key ?: "list") to list,
                "pagination" to pagination
        )
        extraData?.let {
            return okResponse(pageData.plus(it))
        }
        return okResponse(pageData)
    }
    return errorResponse(response.toString())
}

abstract class DocumentFilterRequest(
        var createTimeStart: Date? = null,
        var createTimeEnd: Date? = null,

        var pageIndex: Int = 1,
        var pageSize: Int = 15,
        var sorts: String? = null
) {
    abstract fun queryBuilder() : BoolQueryBuilder

    abstract fun sort(): SortBuilder<*>
}