package com.example.account.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

class JwtUser (var type: String, var id: Long) {

    companion object {
        private const val JWT_USER_SEPARATOR = "#"
        private const val JWT_USER_VALUES_LEN = 2
        fun fromString(value: String): JwtUser? {
            val values = value.split(JWT_USER_SEPARATOR)
            if (JWT_USER_VALUES_LEN != values.size) {
                return null
            }
            return JwtUser(values[0], values[1].toLong())
        }
    }

    override fun toString() = "${type}${JWT_USER_SEPARATOR}${id}"

    fun check(): Boolean {
        if (type.isEmpty()) {
            return false
        }
        if (id == 0L) {
            return false
        }
        return true
    }
}

@Component
@ConfigurationProperties(prefix = "jwt.config")
object JwtConfig {
    lateinit var generateKey: String
    lateinit var duration: String
    lateinit var parseKeys: String
}

fun generateJwt(jwtConfig: JwtConfig, jwtUser: JwtUser): String {
    val now = Date()
    val builder = Jwts.builder()
    builder.setSubject(jwtUser.toString())
        .setIssuedAt(now)
        .setExpiration(Date(now.time + jwtConfig.duration.toLong()))
        .signWith(SignatureAlgorithm.HS512, jwtConfig.generateKey)
    return builder.compact()
}

fun parseJwt(jwtConfig: JwtConfig, jwt: String): JwtUser? {
    val claims = parseJwtClaims(jwtConfig, jwt) ?: return null
    val subject = claims.subject ?: return null
    return JwtUser.fromString(subject)
}

private fun parseJwtClaims(jwtConfig: JwtConfig, jwt: String): Claims? {
    val parser = Jwts.parser()
    if (jwtConfig.parseKeys.isNotEmpty()) {
        val keys = jwtConfig.parseKeys.split(",")
        for (key in keys) {
            parseJwtClaims(parser, key, jwt)?.let {
                return it
            }
        }
    }
    if (jwtConfig.generateKey.isNotEmpty()) {
        return parseJwtClaims(parser, jwtConfig.generateKey, jwt)
    }
    return null
}

private fun parseJwtClaims(parser: JwtParser, key: String, jwt: String): Claims? {
    return try {
        parser.setSigningKey(key)
            .parseClaimsJws(jwt)
            .body
    } catch (e: Exception) {
        return null
    }
}