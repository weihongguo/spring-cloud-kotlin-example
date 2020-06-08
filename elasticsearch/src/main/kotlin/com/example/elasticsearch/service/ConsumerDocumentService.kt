package com.example.elasticsearch.service

import com.example.elasticsearch.document.ConsumerDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

interface ConsumerDocumentService : BaseDocumentService<ConsumerDocument> {
    fun save(consumerDoc: ConsumerDocument): ConsumerDocument
}

@Service
class ConsumerDocumentServiceImpl : BaseDocumentServiceImpl<ConsumerDocument>(), ConsumerDocumentService {

    @Autowired
    lateinit var consumerDocumentRepository: ConsumerDocumentRepository

    override fun getDocumentRepository(): BaseDocumentRepository<ConsumerDocument> {
        return consumerDocumentRepository
    }

    override fun save(consumerDoc: ConsumerDocument): ConsumerDocument {
        return consumerDocumentRepository.save(consumerDoc)
    }
}

@Repository
interface ConsumerDocumentRepository : BaseDocumentRepository<ConsumerDocument>