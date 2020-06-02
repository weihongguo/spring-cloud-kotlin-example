package com.example.base

import java.lang.RuntimeException

class RequestException(message: String? = null) : RuntimeException(message ?: "请求参数错误")

class JwtAuthorizationRequest(var jwt: String, var module: String) {

    fun check(): Boolean {
        if (jwt.isBlank() || module.isBlank()) {
            return false
        }
        return true
    }
}