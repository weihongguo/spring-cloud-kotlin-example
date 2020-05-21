package com.example.account.service

import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/5/21 14:13
 **/

interface UserRoleService : BaseService<UserRole>

@Service
class UserRoleServiceImpl : BaseServiceImpl<UserRole>(), UserRoleService {

    @Autowired
    lateinit var rolePermissionRepository: UserRoleRepository

    override fun getRepository(): BaseRepository<UserRole> {
        return rolePermissionRepository
    }
}

@Repository
interface UserRoleRepository : BaseRepository<UserRole>