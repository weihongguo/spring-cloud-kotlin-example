package com.example.account.service

import com.example.account.repository.UserRepository
import com.example.account.security.JwtUser
import com.example.database.domain.user.User
import com.example.database.repository.BaseRepository
import com.example.database.service.BaseService
import com.example.database.service.BaseServiceImpl
import com.example.security.AuthorizationUserType
import org.apache.tomcat.util.http.parser.Authorization
import org.springframework.beans.factory.annotation.Autowired
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