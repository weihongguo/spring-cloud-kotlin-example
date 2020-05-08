package com.example.base

data class JwtAuthorizationRequest(
    var jwt: String,
    var module: String
) {
    fun check(): Boolean {
        if (jwt.isEmpty() || module.isEmpty()) {
            return false
        }
        return true
    }
}