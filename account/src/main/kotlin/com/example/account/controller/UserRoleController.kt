package com.example.account.controller

import com.example.account.service.RolePermissionService
import com.example.base.Response
import com.example.base.account.RolePermission
import com.example.base.okResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:11
 **/
@RestController
@RequestMapping("user_role")
class UserRoleController {

    @Autowired
    lateinit var userRoleService: RolePermissionService

    @PostMapping
    @PreAuthorize("hasPermission('/account/user_role', 'WRITE')")
    fun store(@RequestBody userRole: RolePermission): Response? {
        val savedUserRole = userRoleService.save(userRole)
        return okResponse(mapOf(
            "userRole" to savedUserRole
        ))
    }

    @DeleteMapping("{id}/destroy")
    @PreAuthorize("hasPermission('/account/user_role/{id}/destroy', 'DELETE')")
    fun destroy(@PathVariable("id") id: Long): Response? {
        userRoleService.destroy(id)
        return okResponse()
    }
}