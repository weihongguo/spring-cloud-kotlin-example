package com.example.database.entity

import java.util.*
import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/6/2 14:20
 **/

@Entity
data class ScheduleJob (
    var module: String = "",
    var name: String = "",
    var lockTime: Date? = null,
    var lockUntil: Date? = null,
    var lockBy: String? = null
) : BaseEntity()

@Entity
data class ScheduleLog (
    var scheduleJobId: Long = -1,
    var processBy: String = "",
    var startTime: Date? = null,
    var endTime: Date? = null,
    var result: String = "",
    var resultInfo: String? = null
) : BaseEntity()

enum class ScheduleResultEnum (var value: String, var label: String) {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败")
}