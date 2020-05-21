package com.example.account.controller

import com.example.account.service.RoleService
import com.example.base.Response
import com.example.base.okResponse
import com.example.database.FilterRequest
import com.example.database.entity.Role
import com.example.database.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("role")
class RoleController {

    @Autowired
    lateinit var roleService: RoleService

    @PostMapping
    @PreAuthorize("hasPermission('/account/role', 'WRITE')")
    fun store(@RequestBody role: Role): Response {
        val savedRole = roleService.save(role)
        return okResponse(mapOf(
            "role" to savedRole
        ))
    }

    @PutMapping("{id}")
    @PreAuthorize("hasPermission('/account/role/{id}', 'WRITE')")
    fun update(@PathVariable("id") id: Long, @RequestBody role: Role): Response {
        role.id = id
        val updatedRole = roleService.update(role)
        return okResponse(mapOf(
            "role" to updatedRole
        ))
    }

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/account/role/{id}', 'READ')")
    fun show(@PathVariable("id") id: Long): Response {
        val role = roleService.getById(id)
        return okResponse(mapOf(
            "role" to role
        ))
    }

    @PostMapping("page")
    @PreAuthorize("hasPermission('/account/role/page', 'READ')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = roleService.page(request)
        return pageResponse(page, "roles")
    }
}