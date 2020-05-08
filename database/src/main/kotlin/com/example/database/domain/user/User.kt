package com.example.database.domain.user

import com.example.database.domain.BaseEntity
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