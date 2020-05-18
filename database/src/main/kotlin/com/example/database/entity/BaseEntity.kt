package com.example.database.entity

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

class EntityNotFoundException(message: String? = null) : RuntimeException(message ?: "资源不存在")

class EntityExistedException(message: String? = null) : RuntimeException(message ?: "资源已经存在")

class EntityOperateException(message: String? = null) : RuntimeException(message ?: "资源操作错误")

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var createTime: Date? = null,
    var updateTime: Date? = null,
    var deleteTime: Date? = null
)