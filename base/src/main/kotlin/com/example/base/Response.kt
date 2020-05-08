package com.example.base

import com.fasterxml.jackson.databind.ObjectMapper
import javax.servlet.http.HttpServletResponse

data class Response(var code: Int, var message: String, var data: Map<String, Any>? = null)

fun unauthorizedResponse(data: Map<String, Any>? = null): Response {
    return Response(HttpServletResponse.SC_UNAUTHORIZED, "", data)
}

fun forbiddenResponse(data: Map<String, Any>? = null): Response {
    return Response(HttpServletResponse.SC_FORBIDDEN, "", data)
}

fun okResponse(data: Map<String, Any>? = null): Response {
    return Response(HttpServletResponse.SC_OK, "", data)
}

fun errorResponse(message: String, data: Map<String, Any>? = null): Response {
    return Response(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, data)
}

fun notFoundResponse(message: String, data: Map<String, Any>? = null): Response {
    return Response(HttpServletResponse.SC_FORBIDDEN, message, data)
}

fun <T> getResponseData(response: Response, key: String, clazz: Class<T>): T? {
    var data = response.data ?: throw RuntimeException("data null")
    var result = data[key] ?: throw RuntimeException("data of $key null")
    return try {
        ObjectMapper().convertValue(result, clazz)
    } catch (e: Exception) {
        throw RuntimeException("convertValue: ${e.toString()}")
    }
}