package com.example.account.service

import com.example.account.request.InitRequest
import com.example.account.request.RegisterRequest
import com.example.base.ModelExistedException
import com.example.base.RequestException
import com.example.base.account.*
import com.example.base.security.encodePassword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface InitService {
    fun init(request: InitRequest)
}

@Service
class InitServiceImpl : InitService {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userRoleService: UserRoleService
    @Autowired
    lateinit var roleService: RoleService
    @Autowired
    lateinit var rolePermissionService: RolePermissionService
    @Autowired
    lateinit var permissionService: PermissionService

    @Override
    @Transactional(rollbackFor = [Exception::class])
    override fun init(request: InitRequest) {
        if (!request.check()) {
            throw RequestException()
        }
        initAdmin(request.adminUser)
        initNorman(request.normalUser)
    }

    private fun initAdmin(request: RegisterRequest) {
        userService.getByMobile(request.mobile)?.let {
            throw ModelExistedException()
        }
        val permissions = initAdminPermission()
        val role = initRole(RoleTypeEnum.USER, "管理用户", permissions)
        initUser(request, role)
    }

    private fun initNorman(request: RegisterRequest) {
        userService.getByMobile(request.mobile)?.let {
            throw ModelExistedException()
        }
        val permissions = initNormalPermission()
        val role = initRole(RoleTypeEnum.USER, "普通用户", permissions)
        initUser(request, role)
    }

    private fun initUser(request: RegisterRequest, role: Role) {
        val user = User(mobile = request.mobile, password = encodePassword(request.password))
        userService.save(user)
        val userRole = UserRole(userId = user.id!!, roleId = role.id!!)
        userRoleService.save(userRole)
    }

    private fun initRole(type: RoleTypeEnum, name: String, permissions: List<Permission>? = null): Role {
        val role = Role(type = type.value, name = name)
        roleService.save(role)
        permissions?.let {
            if (it.isNotEmpty()) {
                val rolePermissions = ArrayList<RolePermission>()
                for (permission in permissions) {
                    val rolePermission = RolePermission(roleId = role.id!!, permissionId = permission.id!!)
                    rolePermissions.add(rolePermission)
                }
                rolePermissionService.saveAll(rolePermissions)
            }
        }
        return role
    }

    private fun initAdminPermission(): List<Permission> {
        val permissions = ArrayList<Permission>()
        permissions.add(Permission(name = "用户角色管理", module = PermissionModuleEnum.ACCOUNT.value, method = PermissionMethodEnum.ALL.value, pathPattern = "/account/user_role/**"))
        permissions.add(Permission(name = "角色管理", module = PermissionModuleEnum.ACCOUNT.value, method = PermissionMethodEnum.ALL.value, pathPattern = "/account/role/**"))
        permissions.add(Permission(name = "角色权限管理", module = PermissionModuleEnum.ACCOUNT.value, method = PermissionMethodEnum.ALL.value, pathPattern = "/account/role_permission/**"))
        permissions.add(Permission(name = "权限管理", module = PermissionModuleEnum.ACCOUNT.value, method = PermissionMethodEnum.ALL.value, pathPattern = "/account/permission/**"))
        return permissionService.saveAll(permissions)
    }

    private fun initNormalPermission(): List<Permission> {
        val permissions = ArrayList<Permission>()
        permissions.add(Permission(name = "生产者管理", module = PermissionModuleEnum.PRODUCER.value, method = PermissionMethodEnum.ALL.value, pathPattern = "/producer/producer/**"))
        permissions.add(Permission(name = "消费者管理", module = PermissionModuleEnum.CONSUMER.value, method = PermissionMethodEnum.ALL.value, pathPattern = "/consumer/consumer/**"))
        return permissionService.saveAll(permissions)
    }
}