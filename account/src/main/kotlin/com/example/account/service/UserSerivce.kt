package com.example.account.service

import com.example.account.security.JwtUser
import com.example.database.entity.User
import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.security.AuthorizationUserType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface UserService : BaseService<User> {
    fun getByMobile(mobile: String): User?
    fun getByJwtUser(jwtUser: JwtUser): User?
}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByMobile(mobile: String): User?
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