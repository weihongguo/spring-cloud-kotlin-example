package com.example.base.response

import javax.servlet.http.HttpServletResponse

data class Response(var code: Int, var message: String, var data: Map<String, Any>? = null)

fun responseUnauthorized(): Response {
    return Response(HttpServletResponse.SC_UNAUTHORIZED, "")
}

fun responseForbidden(): Response {
    return Response(HttpServletResponse.SC_FORBIDDEN, "")
}

fun responseOk(data: Map<String, Any>? = null): Response {
    return Response(HttpServletResponse.SC_OK, "", data)
}

fun responseError(message: String): Response {
    return Response(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message)
}

fun responseNotFound(message: String): Response {
    return Response(HttpServletResponse.SC_FORBIDDEN, message)
}