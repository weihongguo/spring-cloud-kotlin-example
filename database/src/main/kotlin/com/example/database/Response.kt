package com.example.database

import com.example.base.Response
import com.example.base.okResponse
import com.example.database.entity.BaseEntity
import org.springframework.data.domain.Page

data class Pagination(var pageIndex: Int, var pageSize: Int, var totalElements: Long, var totalPages: Int)

fun<T: BaseEntity> pageResponse(page: Page<T>, key: String? = null, extraData: Map<String, *>? = null): Response {
    val list = page.content
    val pagination = Pagination(pageIndex = page.pageable.pageNumber + 1, pageSize = page.pageable.pageSize, totalElements = page.totalElements, totalPages = page.totalPages)
    val pageData = mapOf(
        (key ?: "list") to list,
        "pagination" to pagination
    )
    extraData?.let {
        return okResponse(pageData.plus(it))
    }
    return okResponse(pageData)
}