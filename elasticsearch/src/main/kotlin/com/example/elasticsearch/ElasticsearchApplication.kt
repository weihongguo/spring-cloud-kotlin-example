package com.example.elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableFeignClients
@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.database.entity"])
@EnableJpaRepositories(basePackages = ["com.example.consumer.service", "com.example.database.service"])
@SpringBootApplication(scanBasePackages = ["com.example.consumer.**", "com.example.base.config", "com.example.database.service"])
class ElasticsearchApplication

fun main(args: Array<String>) {
	runApplication<ElasticsearchApplication>(*args)
}
