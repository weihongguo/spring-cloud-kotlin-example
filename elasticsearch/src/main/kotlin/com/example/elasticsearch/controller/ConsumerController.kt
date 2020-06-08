package com.example.elasticsearch.controller

import com.example.base.Response
import com.example.base.okResponse
import com.example.elasticsearch.service.ConsumerDocumentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}