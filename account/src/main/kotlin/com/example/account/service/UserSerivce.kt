package com.example.account.service

import com.example.account.security.JwtUser
import com.example.base.account.User
import com.example.base.account.UserRole
import com.example.base.BaseRepository
import com.example.base.BaseService
import com.example.base.BaseServiceImpl
import com.example.security.AuthorizationUserType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface UserService : BaseService<User> {
    fun getByMobile(mobile: String): User?
    fun getByJwtUser(jwtUser: JwtUser): User?
}

@Service
class UserServiceImpl : BaseServiceImpl<User>(), UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getRepository(): BaseRepository<User> {
        return userRepository
    }

    override fun getByMobile(mobile: String): User? {
        return userRepository.findByMobile(mobile)
    }

    override fun getByJwtUser(jwtUser: JwtUser): User? {
        if (jwtUser.type != AuthorizationUserType.USER.value) {
            return null
        }
        return userRepository.findById(jwtUser.id).orElse(null)
    }
}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByMobile(mobile: String): User?
}

/* */

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

