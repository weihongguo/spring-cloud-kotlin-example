package com.example.base.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory

/**
 * @Author：GuoGuo
 * @Date 2020/6/2 16:31
 **/

@Configuration
class ScheduleConfig : SchedulingConfigurer {

    private var count = 1

    override fun configureTasks(registrar: ScheduledTaskRegistrar) {
        val threadFactory = ThreadFactory { runnable ->
            count++
            Thread(runnable, "schedule-${count}")
        }
        val threadPoolExecutor = ScheduledThreadPoolExecutor(4, threadFactory)
        registrar.setScheduler(threadPoolExecutor)
    }
}