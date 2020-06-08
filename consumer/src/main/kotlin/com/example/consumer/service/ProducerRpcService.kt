package com.example.consumer.service

import com.example.base.Response
import com.example.base.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 18:12
 **/
@FeignClient(name = "PRODUCER", path = "api/producer", configuration = [FeignConfig::class])
interface ProducerRpcService {

    @GetMapping("producer/{id}")
    fun producerShow(@PathVariable id: Long): Response
}