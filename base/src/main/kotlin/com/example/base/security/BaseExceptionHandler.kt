package com.example.base.security

import com.example.base.*
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 15:13
 **/
abstract class BaseExceptionHandler {

    @ExceptionHandler(AccessDeniedException::class)
    open fun accessDeniedException(request: HttpServletRequest, response: HttpServletResponse, e: AccessDeniedException): Response {
        response.status = HttpServletResponse.SC_FORBIDDEN
        return forbiddenResponse(mapOf(
            "url" to request.requestURL.toString(),
            "exception" to e.toString()
        ))
    }

    @ExceptionHandler(ModelNotFoundException::class)
    open fun notFoundException(request: HttpServletRequest, response: HttpServletResponse, e: EntityNotFoundException): Response {
        response.status = HttpServletResponse.SC_NOT_FOUND
        return notFoundResponse(e.message ?: "数据不存在", mapOf(
            "url" to request.requestURL.toString(),
            "exception" to e.toString()
        ))
    }

    @ExceptionHandler
    open fun unknownException(request: HttpServletRequest, response: HttpServletResponse, e: Exception): Response {
        e.printStackTrace()
        response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        return errorResponse(e.message ?: "服务繁忙，请稍后再试", mapOf(
            "url" to request.requestURL.toString(),
            "exception" to e.toString()
        ))
    }
}