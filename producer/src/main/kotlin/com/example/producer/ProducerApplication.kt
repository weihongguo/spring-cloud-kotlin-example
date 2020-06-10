package com.example.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

//@EnableScheduling
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.base.entity"])
@EnableJpaRepositories(basePackages = ["com.example.producer.service", "com.example.base.service"])
@SpringBootApplication(scanBasePackages = ["com.example.producer.**", "com.example.base.config", "com.example.base.service"])
class ProducerApplication

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}
