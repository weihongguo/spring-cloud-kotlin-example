package com.example.consumer.service

import com.example.base.JwtAuthorizationRequest
import com.example.base.Response
import com.example.security.FeignAuthorizationConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 18:10
 **/
@FeignClient(name = "ACCOUNT", path = "api/account", configuration = [FeignAuthorizationConfig::class])
interface AccountService {

    @PostMapping("authorization")
    fun authorization(@RequestBody request: JwtAuthorizationRequest): Response
}