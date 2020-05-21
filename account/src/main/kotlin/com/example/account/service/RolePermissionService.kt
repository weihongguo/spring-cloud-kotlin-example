package com.example.account.service

import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.RolePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:13
 **/

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