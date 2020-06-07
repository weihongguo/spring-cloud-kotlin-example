package com.example.elasticsearch.service

import com.example.elasticsearch.document.ConsumerDoc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface ConsumerDocService {
    fun getById(id: String): ConsumerDoc?
    fun save(consumerDoc: ConsumerDoc): ConsumerDoc
}

@Service
class ConsumerDocServiceImpl : ConsumerDocService {

    @Autowired
    lateinit var consumerDocRepository: ConsumerDocRepository

    override fun getById(id: String): ConsumerDoc? {
        return consumerDocRepository.findById(id).orElse(null)
    }

    override fun save(consumerDoc: ConsumerDoc): ConsumerDoc {
        return consumerDocRepository.save(consumerDoc)
    }
}

@Repository
interface ConsumerDocRepository : ElasticsearchRepository<ConsumerDoc, String> {

}