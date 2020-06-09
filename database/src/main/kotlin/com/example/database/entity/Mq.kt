package com.example.database.entity

import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/6/9 14:37
 **/

@Entity
data class MqFailLog (
    var queue: String? = null,
    var correlationId: String? = null,
    var message: String? = null,
    var operate: String,
    var reason: String
) : BaseEntity()

enum class MqFailLogOperateEnum (var value: String, var label: String) {
    CONFIRM("confirm", "确认"),
    RETURNED_MESSAGE("returned_message", "确认"),
    PROCESS("process", "确认")
}