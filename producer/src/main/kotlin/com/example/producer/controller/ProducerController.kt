package com.example.producer.controller

import com.example.base.Response
import com.example.base.okResponse
import com.example.database.FilterRequest
import com.example.database.entity.Producer
import com.example.database.pageResponse
import com.example.producer.service.ProducerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("producer")
class ProducerController {

    @Autowired
    lateinit var producerService: ProducerService

    @PostMapping
    @PreAuthorize("hasPermission('/producer/producer', 'WRITE')")
    fun store(@RequestBody producer: Producer): Response {
        val saved = producerService.save(producer)
        return okResponse(mapOf(
            "producer" to saved
        ))
    }

    @PutMapping("{id}")
    @PreAuthorize("hasPermission('/producer/producer/{id}', 'WRITE')")
    fun update(@PathVariable id: Long, @RequestBody producer: Producer): Response {
        producer.id = id
        val updated= producerService.update(producer)
        return okResponse(mapOf(
            "producer" to updated
        ))
    }

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/producer/producer/{id}', 'READ')")
    fun show(@PathVariable id: Long): Response {
        val producer = producerService.getById(id)
        return okResponse(mapOf(
            "producer" to producer
        ))
    }

    @DeleteMapping("{id}/remove")
    @PreAuthorize("hasPermission('/producer/producer/{id}/remove', 'DELETE')")
    fun remove(@PathVariable id: Long): Response {
        val removed = producerService.remove(id)
        return okResponse(mapOf(
            "producer" to removed
        ))
    }

    @DeleteMapping("{id}/restore")
    @PreAuthorize("hasPermission('/producer/producer/{id}/restore', 'DELETE')")
    fun restore(@PathVariable id: Long): Response {
        val restored = producerService.restore(id)
        return okResponse(mapOf(
            "producer" to restored
        ))
    }

    @DeleteMapping("{id}/destroy")
    @PreAuthorize("hasPermission('/producer/producer/{id}/destroy', 'DELETE')")
    fun destroy(@PathVariable id: Long): Response {
        producerService.destroy(id)
        return okResponse()
    }

    @PostMapping("page")
    @PreAuthorize("hasPermission('/producer/producer/page', 'POST')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = producerService.page(request)
        return pageResponse(page, "producers")
    }
}