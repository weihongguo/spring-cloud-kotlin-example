package com.example.producer

import com.alibaba.fastjson.parser.ParserConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

//@EnableScheduling
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.database.entity"])
@EnableJpaRepositories(basePackages = ["com.example.producer.service", "com.example.database.service"])
@SpringBootApplication(scanBasePackages = ["com.example.producer.**", "com.example.base.config", "com.example.database.service"])
class ProducerApplication

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}
