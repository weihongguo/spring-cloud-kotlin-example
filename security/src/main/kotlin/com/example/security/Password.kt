package com.example.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun encodePassword(password: CharSequence): String {
    return BCryptPasswordEncoder().encode(password)
}

fun matchesPassword(rawPassword: CharSequence, encodedPassword: String): Boolean {
    return BCryptPasswordEncoder().matches(rawPassword, encodedPassword)
}