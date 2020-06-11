package com.example.base.mq

import com.example.base.BaseModel
import javax.persistence.Entity

@Entity
data class MqLog (
        var queue: String? = null,
        var correlationId: String? = null,
        var message: String? = null,
        var operate: String = "",
        var result: String = "",
        var reason: String = ""
) : BaseModel()

enum class MqLogResultEnum (var value: String, var label: String) {
    SUCCESS("success", "确认"),
    FAIL("fail", "确认"),
}

enum class MqLogOperateEnum (var value: String, var label: String) {
    CONFIRM("confirm", "确认"),
    RETURNED_MESSAGE("returned_message", "确认"),
    PROCESS("process", "确认")
}