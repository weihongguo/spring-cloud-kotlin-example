package com.example.base

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.Page
import javax.servlet.http.HttpServletResponse

data class Response(
    var code: Int,
    var message: String,
    var data: Map<String, *>? = null
) {
    override fun toString(): String = "Response [code=$code message=$message]"

    fun <T> getData(key: String, clazz: Class<T>): T? {
        data?.let {
            val result = it[key] ?: return null
            return try {
                ObjectMapper().convertValue(result, clazz)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
}

fun unauthorizedResponse(data: Map<String, *>? = null): Response {
    return Response(HttpServletResponse.SC_UNAUTHORIZED, "", data)
}

fun forbiddenResponse(data: Map<String, *>? = null): Response {
    return Response(HttpServletResponse.SC_FORBIDDEN, "", data)
}

fun okResponse(data: Map<String, *>? = null): Response {
    return Response(HttpServletResponse.SC_OK, "", data)
}

fun errorResponse(message: String, data: Map<String, *>? = null): Response {
    return Response(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, data)
}

fun notFoundResponse(message: String, data: Map<String, *>? = null): Response {
    return Response(HttpServletResponse.SC_FORBIDDEN, message, data)
}

data class Pagination(var pageIndex: Int, var pageSize: Int, var totalElements: Long, var totalPages: Int)

fun<T: BaseModel> pageResponse(page: Page<T>, key: String? = null, extraData: Map<String, *>? = null): Response {
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