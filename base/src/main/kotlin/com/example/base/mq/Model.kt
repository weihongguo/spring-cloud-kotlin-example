package com.example.base.mq

import com.example.base.BaseModel
import javax.persistence.Entity

@Entity
data class MessageQueueFailLog (
        var queue: String? = null,
        var correlationId: String? = null,
        var message: String? = null,
        var operate: String,
        var reason: String
) : BaseModel()

enum class MessageQueueFailLogOperateEnum (var value: String, var label: String) {
    CONFIRM("confirm", "确认"),
    RETURNED_MESSAGE("returned_message", "确认"),
    PROCESS("process", "确认")
}