package com.example.database.entity

import javax.persistence.Entity

@Entity
data class Consumer(
    var name: String = "",
    var producerId: Long = -1
) : BaseEntity()