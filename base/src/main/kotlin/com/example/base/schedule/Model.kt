package com.example.base.schedule

import com.example.base.BaseModel
import java.util.*
import javax.persistence.Entity


@Entity
data class ScheduleJob (
        var module: String = "",
        var name: String = "",
        var lockTime: Date? = null,
        var lockUntil: Date? = null,
        var lockBy: String? = null
) : BaseModel()

@Entity
data class ScheduleLog (
        var scheduleJobId: Long = -1,
        var processBy: String = "",
        var startTime: Date? = null,
        var endTime: Date? = null,
        var result: String = "",
        var resultInfo: String? = null
) : BaseModel()

enum class ScheduleResultEnum (var value: String, var label: String) {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败")
}