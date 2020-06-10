package com.example.base.schedule

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory

@Configuration
@EnableScheduling
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