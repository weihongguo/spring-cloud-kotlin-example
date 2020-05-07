package com.example.account.service

import com.example.account.repository.UserRepository
import com.example.database.domain.user.User
import com.example.database.repository.base.BaseRepository
import com.example.database.service.base.BaseService
import com.example.database.service.base.BaseServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface UserService : BaseService<User> {
    fun getByMobile(mobile: String): User?
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
}