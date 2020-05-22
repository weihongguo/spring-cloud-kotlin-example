package com.example.producer.service

import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 15:59
 **/
interface TestService : BaseService<Test>

@Service
class TestServiceImpl : BaseServiceImpl<Test>(), TestService {

    @Autowired
    lateinit var testRepository: TestRepository

    override fun getRepository(): BaseRepository<Test> {
        return testRepository
    }
}

@Repository
interface TestRepository : BaseRepository<Test>