package com.example.base.model

import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/6/10 16:48
 **/

@Entity
data class MessageQueueFailLog (
    var queue: String? = null,
    var correlationId: String? = null,
    var message: String? = null,
    var operate: String,
    var reason: String
) : Model()

enum class MessageQueueFailLogOperateEnum (var value: String, var label: String) {
    CONFIRM("confirm", "确认"),
    RETURNED_MESSAGE("returned_message", "确认"),
    PROCESS("process", "确认")
}