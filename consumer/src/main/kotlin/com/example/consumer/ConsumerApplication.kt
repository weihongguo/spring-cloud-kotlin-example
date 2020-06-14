package com.example.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

//@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EntityScan(basePackages = ["com.example.base.consumer", "com.example.base.mq", "com.example.base.schedule"])
@EnableJpaRepositories(basePackages = ["com.example.consumer", "com.example.base.consumer", "com.example.base.mq", "com.example.base.schedule"])
@SpringBootApplication(scanBasePackages = ["com.example.consumer", "com.example.base.consumer", "com.example.base.mq", "com.example.base.schedule", "com.example.base.feign", "com.example.base.cache"])
class ConsumerApplication

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
