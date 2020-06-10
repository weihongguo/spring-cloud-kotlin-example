package com.example.account.controller

import com.example.account.service.UserService
import com.example.base.FilterRequest
import com.example.base.Response
import com.example.base.okResponse
import com.example.base.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:47
 **/
@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/account/user/{id}', 'READ')")
    fun show(@PathVariable("id") id: Long): Response {
        val user = userService.getById(id)
        return okResponse(mapOf(
            "user" to user
        ))
    }

    @PostMapping("page")
    @PreAuthorize("hasPermission('/account/user/page', 'READ')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = userService.page(request)
        return pageResponse(page, "users")
    }
}