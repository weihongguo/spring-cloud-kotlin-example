package com.example.security

class Authorization (
    var user: AuthorizationUser? = null,
    var permissionAuthorities: Collection<PermissionAuthority>? = null
)

interface AuthorizationService {
    fun getByJwt(jwt: String): Authorization?
}

class AuthorizationUser (
    var type: String = "",
    var id: Long = 0L,
    var nickname: String = "",
    var avatarUrl: String = ""
)

enum class AuthorizationUserType(var value: String, var label: String) {
    USER("user", "用户")
}