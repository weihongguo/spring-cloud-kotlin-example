package com.example.account.request

data class LoginRequest(var mobile: String, var password: String) {

    fun check(): Boolean {
        if (mobile.isEmpty() || password.isEmpty()) {
            throw RuntimeException("请求错误")
        }
        return true
    }
}