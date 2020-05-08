package com.example.account.controller

import com.example.account.utils.Single
import com.example.account.utils.spaceToStar
import com.example.base.Response
import com.example.base.okResponse
import com.example.database.entity.NotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.access.AccessDeniedException

/**
 * @Author：GuoGuo
 * @Date 2020/5/6 16:48
 **/
@RestController
@RequestMapping("test")
class TestController {

    @GetMapping("hello")
    fun hello(name: String, test: String): Response {
        println(test)
        when (test) {
            "forbidden" -> throw AccessDeniedException("无法访问")
            "not-found" -> throw NotFoundException()
            "runtime" -> throw RuntimeException()
            else -> println(test)
        }

        val list = listOf("a", "b", "c")
        list.plus("d")
        println(list)
        val map = mapOf("a" to 1, "b" to 2, "c" to 3)
        map.plus(Pair("d", 4))
        println(map)
        val p: String by lazy {
            "hello"
        }
        println(p)
        println("Convert this to camelcase".spaceToStar())
        println(Single.name)
        Single.name = "hi"
        println(Single.name)

        return okResponse()
    }
}