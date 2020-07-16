package com.example.base

import java.io.Serializable
import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * @Author：GuoGuo
 * @Date 2020/6/10 16:41
 **/

class ModelNotFoundException(message: String? = null) : RuntimeException(message ?: "资源不存在")

class ModelExistedException(message: String? = null) : RuntimeException(message ?: "资源已经存在")

class ModelOperateException(message: String? = null) : RuntimeException(message ?: "资源操作错误")

@MappedSuperclass
abstract class BaseModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var createTime: Date? = null,
    var updateTime: Date? = null,
    var deleteTime: Date? = null
) : Serializable