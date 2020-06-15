package com.example.elasticsearch.service

import com.alibaba.fastjson.JSON
import com.example.base.Pagination
import com.example.base.Response
import com.example.base.okResponse
import com.example.elasticsearch.document.BaseDocument
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.rest.RestStatus
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.elasticsearch.search.sort.SortOrder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class DocumentService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var client: RestHighLevelClient

    fun <T: BaseDocument> save(document: T): T? {
        val indexRequest = IndexRequest(document.index())
            .id(document.id)
            .source(JSON.toJSONString(document), XContentType.JSON)
        val indexResponse = client.index(indexRequest, RequestOptions.DEFAULT)
        if (indexResponse.status() == RestStatus.OK) {
            return document
        }
        log.error(indexResponse.status().toString())
        return null
    }

    fun <T: BaseDocument> saveAll(documents: Iterable<T>): Iterable<T>? {
        val bulkRequest = BulkRequest()
        documents.forEach{
            val indexRequest = IndexRequest(it.index())
                .id(it.id)
                .source(JSON.toJSONString(it), XContentType.JSON)
            bulkRequest.add(indexRequest)
        }
        val bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT)
        if (bulkResponse.status() == RestStatus.OK) {
            return documents
        }
        return null
    }

    fun <T: BaseDocument> getById(index: String, id: String, clazz: Class<T>): T? {
        val getRequest = GetRequest(index, id)
        val getResponse = client.get(getRequest, RequestOptions.DEFAULT)
        if (getResponse.isExists) {
            return JSON.parseObject(getResponse.sourceAsString, clazz)
        }
        return null
    }

    fun <T: BaseDocument> list(index: String, filterRequest: DocumentFilterRequest, clazz: Class<T>): Iterable<T>? {
        val sourceBuilder = SearchSourceBuilder()
            .query(filterRequest.queryBuilder())
        filterRequest.sorts().forEach { sortBuilder ->
            sourceBuilder.sort(sortBuilder)
        }
        val searchRequest = SearchRequest(index).source(sourceBuilder)
        val searchResponse =  client.search(searchRequest, RequestOptions.DEFAULT)
        if (searchResponse.status() == RestStatus.OK) {
            val hits = searchResponse.hits
            val list: MutableList<T> = mutableListOf()
            hits.forEach {
                list.add(JSON.parseObject(it.sourceAsString, clazz))
            }
            return list
        }
        throw RuntimeException(searchResponse.status().toString())
    }

    fun <T: BaseDocument> page(index: String, filterRequest: DocumentFilterRequest, clazz: Class<T>) : Page<T> {
        val sourceBuilder = SearchSourceBuilder()
            .from(filterRequest.pageIndex - 1)
            .size(filterRequest.pageSize)
            .query(filterRequest.queryBuilder())
        filterRequest.sorts().forEach { sortBuilder ->
            sourceBuilder.sort(sortBuilder)
        }
        val searchRequest = SearchRequest(index).source(sourceBuilder)
        val searchResponse =  client.search(searchRequest, RequestOptions.DEFAULT)
        if (searchResponse.status() == RestStatus.OK) {
            val hits = searchResponse.hits
            val list: MutableList<T> = mutableListOf()
            hits.forEach {
                list.add(JSON.parseObject(it.sourceAsString, clazz))
            }
            return Page<T>(
                pageIndex = filterRequest.pageIndex,
                pageSize = filterRequest.pageSize,
                totalElements = hits.totalHits?.value ?: 0,
                content = list
            )
        }
        throw RuntimeException(searchResponse.status().toString())
    }
}

class Page<T: BaseDocument>(
    val pageIndex: Int,
    val pageSize: Int,
    val totalElements: Long,
    val content: Iterable<T>
)

fun <T: BaseDocument> pageResponse(page: Page<T>, key: String? = null, extraData: Map<String, *>? = null): Response {
    var totalPages = page.totalElements / page.pageSize
    if ((page.totalElements % page.pageSize) > 0) {
        totalPages++
    }
    val pagination = Pagination(
        pageIndex = page.pageIndex,
        pageSize = page.pageSize,
        totalElements = page.totalElements,
        totalPages = totalPages.toInt()
    )
    val pageData = mapOf(
        (key ?: "list") to page.content,
        "pagination" to pagination
    )
    extraData?.let {
        return okResponse(pageData.plus(it))
    }
    return okResponse(pageData)
}

abstract class DocumentFilterRequest(
    var createTimeStart: Date? = null,
    var createTimeEnd: Date? = null,

    var pageIndex: Int = 1,
    var pageSize: Int = 15,
    private var sorts: String? = null
) {

    fun check(): Boolean {
        if (pageIndex <= 0) {
            pageIndex = 1
        }
        if (pageSize <= 0) {
            pageSize = 15
        }
        return true
    }

    abstract fun queryBuilder(): BoolQueryBuilder

    open fun getDefaultFieldDirection(): String? {
        return null
    }

    fun sorts(): List<SortBuilder<*>> {
        val orderList: MutableList<SortBuilder<*>> = ArrayList()
        sorts?.let {
            val orders = it.split(",")
            for (order in orders) {
                val fieldDirection = order.trim().split(" ")
                if (fieldDirection.size == 2) {
                    val direction = if (fieldDirection[1] == "desc") SortOrder.DESC else SortOrder.ASC
                    orderList.add(FieldSortBuilder(fieldDirection[0]).order(direction))
                } else {
                    orderList.add(FieldSortBuilder(fieldDirection[0]).order(SortOrder.ASC))
                }
            }
        }
        getDefaultFieldDirection()?.let {
            val fieldDirection = it.trim().split(" ")
            val direction = if (fieldDirection[1] == "desc") SortOrder.DESC else SortOrder.ASC
            orderList.add(FieldSortBuilder(fieldDirection[0]).order(direction))
        }
        return orderList
    }
}