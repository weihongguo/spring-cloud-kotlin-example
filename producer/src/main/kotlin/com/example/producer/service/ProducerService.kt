package com.example.producer.service

import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.Producer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 15:59
 **/
interface ProducerService : BaseService<Producer>

@Service
class ProducerServiceImpl : BaseServiceImpl<Producer>(), ProducerService {

    @Autowired
    lateinit var producerRepository: ProducerRepository

    override fun getRepository(): BaseRepository<Producer> {
        return producerRepository
    }
}

@Repository
interface ProducerRepository : BaseRepository<Producer>