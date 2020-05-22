package com.example.account.request

class RegisterRequest(var mobile: String, var password: String) {

    fun check(): Boolean {
        if (mobile.isBlank() || password.isBlank()) {
            return false
        }
        return true
    }
}

class LoginRequest(var mobile: String, var password: String) {

    fun check(): Boolean {
        if (mobile.isBlank() || password.isBlank()) {
            return false
        }
        return true
    }
}

class PasswordUpdateRequest(var old: String, var new: String) {

    fun check(): Boolean {
        if (old.isBlank() || new.isBlank()) {
            return false
        }
        return true
    }
}