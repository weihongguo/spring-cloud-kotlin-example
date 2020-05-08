package com.example.database.domain.role

import com.example.database.domain.BaseEntity

data class Role (
    var type: String = "",
    var name: String = ""
) : BaseEntity()

data class RolePermission (
    var roleId: Long = 0,
    var permissionId: Long = 0
) : BaseEntity()

data class Permission(
    var module: String = "",
    var name: String = "",
    var method: String = "",
    var pathPattern: String = ""
) : BaseEntity()

enum class PermissionMethod (
    var value: String,
    var label: String
) {
    READ("READ", "只读"),
    WRITE("WRITE", "读写"),
    ALL("ALL", "所有")
}