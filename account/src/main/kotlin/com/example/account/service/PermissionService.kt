package com.example.account.service

import com.example.base.account.Permission
import com.example.base.account.Role
import com.example.base.account.RolePermission
import com.example.base.BaseRepository
import com.example.base.BaseService
import com.example.base.BaseServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface PermissionService : BaseService<Permission> {
    fun listByUserIdAndModule(userId: Long, module: String): List<Permission>
}

@Service
class PermissionServiceImpl : BaseServiceImpl<Permission>(), PermissionService {

    @Autowired
    lateinit var permissionRepository: PermissionRepository

    override fun getRepository(): BaseRepository<Permission> {
        return permissionRepository
    }

    override fun listByUserIdAndModule(userId: Long, module: String): List<Permission> {
        return permissionRepository.findByUserIdAndModule(userId, module)
    }
}

@Repository
interface PermissionRepository : BaseRepository<Permission> {

    @Query(
        "select distinct(P) from Permission as P" +
                " left join RolePermission as RP on RP.permissionId = P.id" +
                " left join UserRole as UR on UR.roleId = RP.roleId" +
                " where UR.userId = :userId" +
                " and P.module = :module"
    )
    fun findByUserIdAndModule(userId: Long, module: String): List<Permission>
}

/* */

interface RoleService : BaseService<Role>

@Service
class RoleServiceImpl : BaseServiceImpl<Role>(), RoleService {

    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun getRepository(): BaseRepository<Role> {
        return roleRepository
    }
}

@Repository
interface RoleRepository : BaseRepository<Role>

/* */

interface RolePermissionService : BaseService<RolePermission>

@Service
class RolePermissionServiceImpl : BaseServiceImpl<RolePermission>(), RolePermissionService {

    @Autowired
    lateinit var rolePermissionRepository: RolePermissionRepository

    override fun getRepository(): BaseRepository<RolePermission> {
        return rolePermissionRepository
    }
}

@Repository
interface RolePermissionRepository : BaseRepository<RolePermission>