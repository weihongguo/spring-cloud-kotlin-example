package com.example.account.repository

import com.example.database.domain.user.User
import com.example.database.repository.base.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByMobile(mobile: String): User?
}