package com.example.account.service

import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/5/20 16:40
 **/

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