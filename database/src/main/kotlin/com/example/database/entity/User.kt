package com.example.database.entity

import javax.persistence.Entity

@Entity
data class User(
    var mobile: String = "",
    var password: String = ""
) : BaseEntity()

@Entity
data class UserRole(
    var userId: Long = 0,
    var roleId: Long = 0
) : BaseEntity()