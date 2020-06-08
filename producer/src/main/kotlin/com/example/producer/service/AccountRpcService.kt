package com.example.producer.service

import com.example.base.JwtAuthorizationRequest
import com.example.base.Response
import com.example.base.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "ACCOUNT", path = "api/account", configuration = [FeignConfig::class])
interface AccountRpcService {

    @PostMapping("authorization")
    fun authorization(@RequestBody request: JwtAuthorizationRequest): Response
}