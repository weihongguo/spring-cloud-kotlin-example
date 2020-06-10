package com.example.base.account

import com.example.base.BaseModel
import javax.persistence.Entity

@Entity
data class User(
        var mobile: String = "",
        var password: String = ""
) : BaseModel()

@Entity
data class UserRole(
        var userId: Long = 0,
        var roleId: Long = 0
) : BaseModel()

@Entity
data class Role (
        var type: String = "",
        var name: String = ""
) : BaseModel()

enum class RoleTypeEnum(var value: String, var label: String) {
    USER("user", "用户")
}

@Entity
data class RolePermission(
        var roleId: Long = 0,
        var permissionId: Long = 0
) : BaseModel()

@Entity
data class Permission(
        var name: String = "",
        var module: String = "",
        var method: String = "",
        var pathPattern: String = ""
) : BaseModel()

enum class PermissionMethodEnum(var value: String, var label: String) {
    READ("READ", "只读"),
    WRITE("WRITE", "读写"),
    ALL("ALL", "所有")
}

enum class PermissionModuleEnum(var value: String, var label: String) {
    ACCOUNT("account", "账号"),
    PRODUCER("producer", "生产者"),
    CONSUMER("consumer", "消费者")
}