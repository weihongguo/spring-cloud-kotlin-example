package com.example.elasticsearch.controller

import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import com.example.elasticsearch.document.ConsumerDocument
import com.example.elasticsearch.service.ConsumerDocumentFilterRequest
import com.example.elasticsearch.service.ConsumerDocumentService
import com.example.elasticsearch.service.documentPageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("consumer")
class ConsumerController {

    @Autowired
    lateinit var consumerDocumentService: ConsumerDocumentService

    @GetMapping("{id}")
    fun show(@PathVariable id: Long): Response {
        val consumerDocument = consumerDocumentService.getById(id.toString())
        return okResponse(mapOf(
                "id" to id,
                "consumerDocument" to consumerDocument
        ))
    }

    @PostMapping("page")
    fun page(@RequestBody request: ConsumerDocumentFilterRequest): Response {
        if (!request.check()) {
            throw RequestException()
        }
        val response = consumerDocumentService.page(request)
        return documentPageResponse(request, response, ConsumerDocument::class.java, "consumerDocuments", mapOf(
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