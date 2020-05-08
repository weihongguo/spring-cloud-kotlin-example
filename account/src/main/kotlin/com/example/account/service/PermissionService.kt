package com.example.account.service

import com.example.database.entity.Permission
import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface PermissionService : BaseService<Permission> {
    fun listByUserIdAndModule(userId: Long, module: String): List<Permission>
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

@Service
class PermissionServiceImpl : BaseServiceImpl<Permission>(), PermissionService {

    @Autowired
    lateinit var permissionRepository: PermissionRepository

    override fun getRepository(): BaseRepository<Permission> {
        return permissionRepository
    }

    override fun listByUserIdAndModule(userId: Long, module: String): List<Permission> {
        return permissionRepository.findByUserIdAndModule(userId, module)//To change body of created functions use File | Settings | File Templates.
    }
}