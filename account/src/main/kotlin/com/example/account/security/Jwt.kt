package com.example.account.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

private const val JWT_USER_SEPARATOR = "#"
private const val JWT_USER_VALUES_LEN = 2

data class JwtUser (
    var type: String,
    var id: Long
) {
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

fun String.toJwtUser() : JwtUser? {
    val values: Array<String> = this.split(JWT_USER_SEPARATOR).toTypedArray()
    if (JWT_USER_VALUES_LEN != values.size) {
        return null
    }
    return JwtUser(values[0], values[1].toLong())
}

@Component
@ConfigurationProperties(prefix = "jwt.config")
data class JwtConfig(
    var generateKey: String,
    var duration: Long,
    var parseKeys: String
)

fun generateJwt(jwtConfig: JwtConfig, jwtUser: JwtUser): String {
    var now = Date()
    val builder = Jwts.builder()
    builder.setSubject(jwtUser.toString())
        .setIssuedAt(now)
        .setExpiration(Date(now.time + jwtConfig.duration))
        .signWith(SignatureAlgorithm.HS512, jwtConfig.generateKey)
    return builder.compact()
}

fun parseJwt(jwtConfig: JwtConfig, jwt: String): JwtUser? {
    var claims = parseJwtClaims(jwtConfig, jwt) ?: return null
    var subject = claims.subject ?: return null
    return subject.toJwtUser()
}

private fun parseJwtClaims(jwtConfig: JwtConfig, jwt: String): Claims? {
    var parser = Jwts.parser()
    if (jwtConfig.parseKeys.isNotEmpty()) {
        var keys = jwtConfig.parseKeys.split(",")
        for (key in keys) {
            var claims = parseJwtClaims(parser, key, jwt)
            if (claims != null) {
                return claims
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