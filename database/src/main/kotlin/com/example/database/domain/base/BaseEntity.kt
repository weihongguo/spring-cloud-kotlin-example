package com.example.database.domain.base

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var createTime: Date?,
    var updateTime: Date?,
    var deleteTime: Date?
) {
    constructor(): this(null, null, null, null)
}