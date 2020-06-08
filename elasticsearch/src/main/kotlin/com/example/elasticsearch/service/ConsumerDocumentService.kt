package com.example.elasticsearch.service

import com.example.elasticsearch.document.ConsumerDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface ConsumerDocumentService {
    fun getById(id: String): ConsumerDocument?
    fun save(consumerDoc: ConsumerDocument): ConsumerDocument
}

@Service
class ConsumerDocumentServiceImpl : ConsumerDocumentService {

    @Autowired
    lateinit var consumerDocumentRepository: ConsumerDocumentRepository

    override fun getById(id: String): ConsumerDocument? {
        return consumerDocumentRepository.findById(id).orElse(null)
    }

    override fun save(consumerDoc: ConsumerDocument): ConsumerDocument {
        return consumerDocumentRepository.save(consumerDoc)
    }
}

@Repository
interface ConsumerDocumentRepository : ElasticsearchRepository<ConsumerDocument, String> {

}