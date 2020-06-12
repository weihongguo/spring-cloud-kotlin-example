package com.example.base.schedule

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory

@Configuration
class ScheduleConfig : SchedulingConfigurer {

    @Value("\${schedule.thread.count}")
    var count: Int = 2

    private var tag = 0

    override fun configureTasks(registrar: ScheduledTaskRegistrar) {
        val threadFactory = ThreadFactory { runnable ->
            Thread(runnable, "schedule-${++tag}")
        }
        val threadPoolExecutor = ScheduledThreadPoolExecutor(count, threadFactory)
        registrar.setScheduler(threadPoolExecutor)
    }
}