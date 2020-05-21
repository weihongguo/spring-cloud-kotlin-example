package com.example.account.controller

import com.example.account.service.PermissionService
import com.example.account.service.RoleService
import com.example.base.Response
import com.example.base.okResponse
import com.example.database.FilterRequest
import com.example.database.entity.Permission
import com.example.database.entity.Role
import com.example.database.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * @Author：GuoGuo
 * @Date 2020/5/20 17:03
 **/

@RestController
@RequestMapping("permission")
class PermissionController {

    @Autowired
    lateinit var permissionService: PermissionService

    @PostMapping
    @PreAuthorize("hasPermission('/account/permission', 'WRITE')")
    fun store(@RequestBody permission: Permission): Response {
        val savedPermission = permissionService.save(permission)
        return okResponse(mapOf(
            "permission" to savedPermission
        ))
    }

    @PutMapping("{id}")
    @PreAuthorize("hasPermission('/account/permission/{id}', 'WRITE')")
    fun update(@PathVariable("id") id: Long, @RequestBody permission: Permission): Response {
        permission.id = id
        val updatedPermission = permissionService.update(permission)
        return okResponse(mapOf(
            "permission" to updatedPermission
        ))
    }

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/account/permission/{id}', 'READ')")
    fun show(@PathVariable("id") id: Long): Response {
        val permission = permissionService.getById(id)
        return okResponse(mapOf(
            "permission" to permission
        ))
    }

    @PostMapping("page")
    @PreAuthorize("hasPermission('/account/permission/page', 'READ')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = permissionService.page(request)
        return pageResponse(page, "permissions")
    }
}