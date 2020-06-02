package com.example.database.entity

import javax.persistence.Entity

@Entity
data class Role (
    var type: String = "",
    var name: String = ""
) : BaseEntity()

enum class RoleTypeEnum (var value: String, var label: String) {
    USER("user", "用户")
}

@Entity
data class RolePermission (
    var roleId: Long = 0,
    var permissionId: Long = 0
) : BaseEntity()

@Entity
data class Permission(
    var name: String = "",
    var module: String = "",
    var method: String = "",
    var pathPattern: String = ""
) : BaseEntity()

enum class PermissionMethodEnum (var value: String, var label: String) {
    READ("READ", "只读"),
    WRITE("WRITE", "读写"),
    ALL("ALL", "所有")
}

enum class PermissionModuleEnum (var value: String, var label: String) {
    ACCOUNT("account", "账号"),
    PRODUCER("producer", "生产者"),
    CONSUMER("consumer", "消费者")
}

