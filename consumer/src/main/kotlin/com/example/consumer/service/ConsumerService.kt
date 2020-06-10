package com.example.consumer.service

import com.example.base.BaseRepository
import com.example.base.BaseService
import com.example.base.BaseServiceImpl
import com.example.base.consumer.Consumer
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