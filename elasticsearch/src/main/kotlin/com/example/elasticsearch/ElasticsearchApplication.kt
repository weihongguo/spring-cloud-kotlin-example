package com.example.elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(basePackages = ["com.example.base.mq"])
@EnableJpaRepositories(basePackages = ["com.example.elasticsearch", "com.example.base.mq"])
@SpringBootApplication(scanBasePackages = ["com.example.elasticsearch", "com.example.base.mq", "com.example.base.feign"])
class ElasticsearchApplication

fun main(args: Array<String>) {
	runApplication<ElasticsearchApplication>(*args)
}
