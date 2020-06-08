package com.example.consumer.service

import com.example.base.JwtAuthorizationRequest
import com.example.base.Response
import com.example.base.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 18:10
 **/
@FeignClient(name = "ACCOUNT", path = "api/account", configuration = [FeignConfig::class])
interface AccountRpcService {

    @PostMapping("authorization")
    fun authorization(@RequestBody request: JwtAuthorizationRequest): Response
}