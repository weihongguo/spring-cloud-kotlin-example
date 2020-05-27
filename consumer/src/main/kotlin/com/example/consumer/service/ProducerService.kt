package com.example.consumer.service

import com.example.base.JwtAuthorizationRequest
import com.example.base.Response
import com.example.security.FeignAuthorizationConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 18:12
 **/
@FeignClient(name = "PRODUCER", path = "api/producer/producer", configuration = [FeignAuthorizationConfig::class])
interface ProducerService {

    @GetMapping("{id}")
    fun show(@PathVariable id: Long): Response
}