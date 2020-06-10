package com.example.base.model

import java.util.*
import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/6/10 16:47
 **/

@Entity
data class ScheduleJob (
    var module: String = "",
    var name: String = "",
    var lockTime: Date? = null,
    var lockUntil: Date? = null,
    var lockBy: String? = null
) : Model()

@Entity
data class ScheduleLog (
    var scheduleJobId: Long = -1,
    var processBy: String = "",
    var startTime: Date? = null,
    var endTime: Date? = null,
    var result: String = "",
    var resultInfo: String? = null
) : Model()

enum class ScheduleResultEnum (var value: String, var label: String) {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败")
}