package com.example.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.database.entity"])
class ConsumerApplication

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
