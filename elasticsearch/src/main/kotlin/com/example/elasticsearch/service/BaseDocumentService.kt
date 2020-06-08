package com.example.elasticsearch.service

import com.example.elasticsearch.document.BaseDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.repository.NoRepositoryBean

interface BaseDocumentService<T: BaseDocument> {
    fun getDocumentRepository(): BaseDocumentRepository<T>
    fun getById(id: String): T?
}

abstract class BaseDocumentServiceImpl<T: BaseDocument> : BaseDocumentService<T> {

    override fun getById(id: String): T? {
        return getDocumentRepository().findById(id).orElse(null)
    }
}

@NoRepositoryBean
interface BaseDocumentRepository<T: BaseDocument> : ElasticsearchRepository<T, String>