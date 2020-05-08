package com.example.database.entity

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    var createTime: Date? = null,
    var updateTime: Date? = null,
    var deleteTime: Date? = null
)

class NotFoundException(message: String? = null) : RuntimeException(message)