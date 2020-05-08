package com.example.account.service

import com.example.account.repository.PermissionRepository
import com.example.database.domain.role.Permission
import com.example.database.repository.BaseRepository
import com.example.database.service.BaseService
import com.example.database.service.BaseServiceImpl
import org.springframework.beans.factory.annotation.Autowired
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
        return permissionRepository.findByUserIdAndModule(userId, module)//To change body of created functions use File | Settings | File Templates.
    }
}