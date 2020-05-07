package com.example.database.domain.user

import com.example.database.domain.base.BaseEntity
import javax.persistence.Entity

@Entity
data class User(
    var mobile: String,
    var password: String
) : BaseEntity() {
    constructor(): this("", "")
}