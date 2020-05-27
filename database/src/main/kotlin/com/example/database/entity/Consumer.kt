package com.example.database.entity

data class Consumer(
    var name: String = "",
    var producerId: Long = -1
) : BaseEntity()