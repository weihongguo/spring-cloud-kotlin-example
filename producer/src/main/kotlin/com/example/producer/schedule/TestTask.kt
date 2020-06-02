package com.example.producer.schedule

import com.example.database.service.ScheduleJobService
import com.example.database.service.ScheduleLogService
import com.example.database.entity.ScheduleResultEnum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

/**
 * @Author：GuoGuo
 * @Date 2020/6/2 15:04
 **/

@Component
class TestTask {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var scheduleJobService: ScheduleJobService
    @Autowired
    lateinit var scheduleLogService: ScheduleLogService

    @Scheduled(cron = "0/10 * * * * ?")
    fun process() {
        val scheduleJob = scheduleJobService.canRun("schedule", "test", 3L * 1000, "producer")
        scheduleJob?.let {
            val startTime = Date()
            log.info("TestTask process start")
            Thread.sleep(1L * 1000)
            log.info("TestTask process end")
            val endTime = Date()
            scheduleLogService.record(it, startTime, endTime, ScheduleResultEnum.SUCCESS, "success")
            return
        }
        log.info("TestTask do not need run")
    }
}