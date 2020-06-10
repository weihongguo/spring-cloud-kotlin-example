package com.example.elasticsearch.service

import com.example.base.Response
import com.example.base.feign.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "CONSUMER", path = "api/consumer", configuration = [FeignConfig::class])
interface ConsumerRpcService {

    @GetMapping("consumer/{id}")
    fun consumerShow(@PathVariable id: Long, @RequestHeader("Authorization") authorization: String): Response
}