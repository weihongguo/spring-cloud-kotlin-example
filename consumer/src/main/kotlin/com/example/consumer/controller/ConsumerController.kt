package com.example.consumer.controller

import com.example.base.Response
import com.example.base.getResponseData
import com.example.base.okResponse
import com.example.consumer.service.ConsumerService
import com.example.consumer.service.ProducerRpcService
import com.example.database.FilterRequest
import com.example.database.entity.Consumer
import com.example.database.entity.EntityNotFoundException
import com.example.database.entity.Producer
import com.example.database.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 18:12
 **/
@RestController
@RequestMapping("consumer")
class ConsumerController {

    @Autowired
    lateinit var consumerService: ConsumerService
    @Autowired
    lateinit var producerRpcService: ProducerRpcService


    @PostMapping
    @PreAuthorize("hasPermission('/consumer/consumer', 'WRITE')")
    fun store(@RequestBody consumer: Consumer): Response {
        val saved = consumerService.save(consumer)
        return okResponse(mapOf(
            "consumer" to saved
        ))
    }

    @PutMapping("{id}")
    @PreAuthorize("hasPermission('/consumer/consumer/{id}', 'WRITE')")
    fun update(@PathVariable id: Long, @RequestBody consumer: Consumer): Response {
        consumer.id = id
        val updated= consumerService.update(consumer)
        return okResponse(mapOf(
            "consumer" to updated
        ))
    }

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/consumer/consumer/{id}', 'READ')")
    fun show(@PathVariable id: Long): Response {
        val consumer = consumerService.getById(id) ?: throw EntityNotFoundException()
        val rpcResponse = producerRpcService.producerShow(consumer.producerId)
        val producer = getResponseData(rpcResponse, "producer", Producer::class.java)
        return okResponse(mapOf(
            "consumer" to consumer,
            "producer" to producer
        ))
    }

    @DeleteMapping("{id}/remove")
    @PreAuthorize("hasPermission('/consumer/consumer/{id}/remove', 'DELETE')")
    fun remove(@PathVariable id: Long): Response {
        val removed = consumerService.remove(id)
        return okResponse(mapOf(
            "consumer" to removed
        ))
    }

    @DeleteMapping("{id}/restore")
    @PreAuthorize("hasPermission('/consumer/consumer/{id}/restore', 'DELETE')")
    fun restore(@PathVariable id: Long): Response {
        val restored = consumerService.restore(id)
        return okResponse(mapOf(
            "consumer" to restored
        ))
    }

    @DeleteMapping("{id}/destroy")
    @PreAuthorize("hasPermission('/consumer/consumer/{id}/destroy', 'DELETE')")
    fun destroy(@PathVariable id: Long): Response {
        consumerService.destroy(id)
        return okResponse()
    }

    @PostMapping("page")
    @PreAuthorize("hasPermission('/consumer/consumer/page', 'POST')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = consumerService.page(request)
        return pageResponse(page, "consumers")
    }
}