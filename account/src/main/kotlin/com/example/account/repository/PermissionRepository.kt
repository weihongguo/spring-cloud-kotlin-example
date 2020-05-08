package com.example.account.repository

import com.example.database.domain.role.Permission
import com.example.database.repository.BaseRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

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