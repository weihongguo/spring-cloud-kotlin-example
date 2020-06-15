package com.example.elasticsearch.controller

import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import com.example.elasticsearch.document.ConsumerDocument
import com.example.elasticsearch.document.ConsumerDocument.Companion.CONSUMER_INDEX_NAME
import com.example.elasticsearch.document.ConsumerDocumentFilterRequest
import com.example.elasticsearch.service.ConsumerDocumentService
import com.example.elasticsearch.service.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("consumer")
class ConsumerController {

    @Autowired
    lateinit var consumerDocumentService: ConsumerDocumentService

    @GetMapping("{id}")
    fun show(@PathVariable id: Long): Response {
        val consumerDocument = consumerDocumentService.getById(CONSUMER_INDEX_NAME, id.toString(), ConsumerDocument::class.java)
        return okResponse(mapOf(
                "id" to id,
                "consumerDocument" to consumerDocument
        ))
    }

    @PostMapping("page")
    fun page(@RequestBody filterRequest: ConsumerDocumentFilterRequest): Response {
        if (!filterRequest.check()) {
            throw RequestException()
        }
        val page = consumerDocumentService.page(CONSUMER_INDEX_NAME, filterRequest, ConsumerDocument::class.java)
        return pageResponse(page, "consumerDocuments", mapOf(
            "test" to "test"
        ))
    }

    @GetMapping("name_count_map")
    fun nameCountMap(): Response {
        val nameCountMap = consumerDocumentService.nameCountMap()
        return okResponse(mapOf(
                "nameCountMap" to nameCountMap
        ))
    }
}