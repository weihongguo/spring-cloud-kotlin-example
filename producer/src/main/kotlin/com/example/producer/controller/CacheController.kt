package com.example.producer.controller

import com.example.base.Response
import com.example.base.okResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.*
import java.time.Duration

/**
 * @Author：GuoGuo
 * @Date 2020/6/5 10:48
 **/

@RestController
@RequestMapping("cache")
class CacheController {

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate

    @GetMapping("get/{key}")
    fun get(@PathVariable key: String): Response {
        val value = redisTemplate.boundValueOps(key).get()
        return okResponse(mapOf(
            "value" to value
        ))
    }

    @PostMapping("set/{key}/{value}")
    fun set(@PathVariable key: String, @PathVariable value: String): Response {
        val operate = redisTemplate.boundValueOps(key)
        operate.set(value, Duration.ofSeconds(1 * 60 * 60))
        return okResponse(mapOf(
            "value" to operate.get()
        ))
    }

    @DeleteMapping("del/{key}")
    fun del(@PathVariable key: String): Response {
        val deleted = redisTemplate.delete(key)
        return okResponse(mapOf(
            "deleted" to deleted
        ))
    }
}