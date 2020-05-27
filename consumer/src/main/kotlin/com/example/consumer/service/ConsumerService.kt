package com.example.consumer.service

import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.Consumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface ConsumerService : BaseService<Consumer>

@Service
class ConsumerServiceImpl : BaseServiceImpl<Consumer>(), ConsumerService {

    @Autowired
    lateinit var consumerRepository: ConsumerRepository

    override fun getRepository(): BaseRepository<Consumer> {
        return consumerRepository
    }
}

@Repository
interface ConsumerRepository : BaseRepository<Consumer>