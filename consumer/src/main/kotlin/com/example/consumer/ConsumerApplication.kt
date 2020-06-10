package com.example.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

//@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.base.model"])
@EnableJpaRepositories(basePackages = ["com.example.consumer.service", "com.example.base.service"])
@SpringBootApplication(scanBasePackages = ["com.example.consumer", "com.example.base.config", "com.example.base.service"])
class ConsumerApplication

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
