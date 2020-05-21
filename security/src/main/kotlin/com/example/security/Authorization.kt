package com.example.security

data class AuthorizationUser (
    var type: String = "",
    var id: Long = 0L,
    var nickname: String = "",
    var avatarUrl: String = ""
)

enum class AuthorizationUserType(
    var value: String,
    var label: String
) {
    USER("user", "用户")
}

data class Authorization (
    var user: AuthorizationUser,
    var permissionAuthorities: Collection<PermissionAuthority>? = null
)

interface AuthorizationService {
    fun getByJwt(jwt: String): Authorization?
}