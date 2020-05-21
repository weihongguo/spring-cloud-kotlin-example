package com.example.account.request

data class RegisterRequest(var mobile: String, var password: String) {

    fun check(): Boolean {
        if (mobile.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }
}

data class LoginRequest(var mobile: String, var password: String) {

    fun check(): Boolean {
        if (mobile.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }
}

data class PasswordUpdateRequest(var old: String, var new: String) {

    fun check(): Boolean {
        if (old.isEmpty() || new.isEmpty()) {
            return false
        }
        return true
    }
}