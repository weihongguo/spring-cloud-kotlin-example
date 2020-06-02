package com.example.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@EntityScan(basePackages = ["com.example.database.entity"])
@SpringBootApplication
class AccountApplication

fun main(args: Array<String>) {
	runApplication<AccountApplication>(*args)
}
