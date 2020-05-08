package com.example.security

import com.example.database.entity.Permission
import com.example.database.entity.PermissionMethod
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.util.AntPathMatcher

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

private val logger = LoggerFactory.getLogger(PermissionAuthority::class.java)
data class PermissionAuthority(var permission: Permission) : GrantedAuthority {

    override fun getAuthority(): String? {
        return this.permission.name
    }

    fun check(antPathMatcher: AntPathMatcher, path: String?, method: String): Boolean {
        logger.info(permission.method + "#" + permission.pathPattern)
        if (!antPathMatcher.match(permission.pathPattern, path!!)) {
            return false
        }
        val permissionMethod: String = permission.method
        if (permissionMethod == PermissionMethod.ALL.value) {
            return true
        } else if (permissionMethod == PermissionMethod.WRITE.value) {
            if (method == PermissionMethod.WRITE.value || method == PermissionMethod.READ.value) {
                return true
            }
        } else if (permissionMethod == PermissionMethod.READ.value) {
            if (method == PermissionMethod.READ.value) {
                return true
            }
        }
        return false
    }
}

data class Authorization (
    var user: AuthorizationUser,
    var permissionAuthorities: Collection<PermissionAuthority>? = null
)

interface AuthorizationService {
    fun getByJwt(jwt: String): Authorization?
}