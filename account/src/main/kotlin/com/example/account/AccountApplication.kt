package com.example.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.base.account"])
@EnableJpaRepositories(basePackages = ["com.example.account"])
@SpringBootApplication(scanBasePackages = ["com.example.account"])
class AccountApplication

fun main(args: Array<String>) {
	runApplication<AccountApplication>(*args)
}
