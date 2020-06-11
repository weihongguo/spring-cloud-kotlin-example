package com.example.base.schedule

import com.example.base.FilterRequest
import com.example.base.Response
import com.example.base.okResponse
import com.example.base.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author：GuoGuo
 * @Date 2020/6/11 13:27
 **/

@RestController
@RequestMapping("schedule_job")
class ScheduleJobController {

    @Autowired
    lateinit var scheduleJobService: ScheduleJobService

    @PostMapping
    // @PreAuthorize("hasPermission('/**/schedule_job', 'WRITE')")
    fun store(@RequestBody scheduleJob: ScheduleJob): Response {
        val saved = scheduleJobService.save(scheduleJob)
        return okResponse(mapOf(
            "scheduleJob" to saved
        ))
    }

    @PostMapping("page")
    // @PreAuthorize("hasPermission('/**/schedule_job/page', 'POST')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = scheduleJobService.page(request)
        return pageResponse(page, "scheduleJobs")
    }
}

@RestController
@RequestMapping("schedule_log")
class ScheduleLogController {

    @Autowired
    lateinit var scheduleLogService: ScheduleLogService

    @PostMapping("page")
    // @PreAuthorize("hasPermission('/**/schedule_log/page', 'POST')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = scheduleLogService.page(request)
        return pageResponse(page, "scheduleLogs")
    }
}