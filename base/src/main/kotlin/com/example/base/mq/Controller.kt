package com.example.base.mq

import com.example.base.FilterRequest
import com.example.base.Response
import com.example.base.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author：GuoGuo
 * @Date 2020/6/11 13:33
 **/
@RestController
@RequestMapping("mq_log")
class MqLogController {

    @Autowired
    lateinit var mqLogService: MqLogService

    @PostMapping("page")
    // @PreAuthorize("hasPermission('/**/mq_log/page', 'POST')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = mqLogService.page(request)
        return pageResponse(page, "mqLogs")
    }
}