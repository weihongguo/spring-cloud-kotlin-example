package com.example.account.controller

import com.example.account.service.RolePermissionService
import com.example.base.Response
import com.example.base.model.RolePermission
import com.example.base.okResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:11
 **/
@RestController
@RequestMapping("role_permission")
class RolePermissionController {

    @Autowired
    lateinit var rolePermissionService: RolePermissionService

    @PostMapping
    @PreAuthorize("hasPermission('/account/role_permission', 'WRITE')")
    fun store(@RequestBody rolePermission: RolePermission): Response? {
        val savedRolePermission = rolePermissionService.save(rolePermission)
        return okResponse(mapOf(
            "rolePermission" to savedRolePermission
        ))
    }

    @DeleteMapping("{id}/destroy")
    @PreAuthorize("hasPermission('/account/role_permission/{id}/destroy', 'DELETE')")
    fun destroy(@PathVariable("id") id: Long): Response? {
        rolePermissionService.destroy(id)
        return okResponse()
    }
}