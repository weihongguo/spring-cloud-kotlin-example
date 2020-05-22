package com.example.producer.controller

import com.example.base.Response
import com.example.base.okResponse
import com.example.database.FilterRequest
import com.example.database.entity.Test
import com.example.database.pageResponse
import com.example.producer.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("test")
class TestController {

    @Autowired
    lateinit var testService: TestService

    @PostMapping
    @PreAuthorize("hasPermission('/producer/test', 'WRITE')")
    fun store(@RequestBody test: Test): Response {
        val savedTest = testService.save(test)
        return okResponse(mapOf(
            "test" to savedTest
        ))
    }

    @PutMapping("{id}")
    @PreAuthorize("hasPermission('/producer/test/{id}', 'WRITE')")
    fun update(@PathVariable id: Long, @RequestBody test: Test): Response {
        test.id = id
        val updatedTest = testService.update(test)
        return okResponse(mapOf(
            "test" to updatedTest
        ))
    }

    @GetMapping("{id}")
    @PreAuthorize("hasPermission('/producer/test/{id}', 'READ')")
    fun show(@PathVariable id: Long): Response {
        val test = testService.getById(id)
        return okResponse(mapOf(
            "test" to test
        ))
    }

    @DeleteMapping("{id}/remove")
    @PreAuthorize("hasPermission('/producer/test/{id}/remove', 'DELETE')")
    fun remove(@PathVariable id: Long): Response {
        val removedTest = testService.remove(id)
        return okResponse(mapOf(
            "test" to removedTest
        ))
    }

    @DeleteMapping("{id}/restore")
    @PreAuthorize("hasPermission('/producer/test/{id}/restore', 'DELETE')")
    fun restore(@PathVariable id: Long): Response {
        val restoredTest = testService.restore(id)
        return okResponse(mapOf(
            "test" to restoredTest
        ))
    }

    @DeleteMapping("{id}/destroy")
    @PreAuthorize("hasPermission('/producer/test/{id}/destroy', 'DELETE')")
    fun destroy(@PathVariable id: Long): Response {
        testService.destroy(id)
        return okResponse()
    }

    @PostMapping("page")
    @PreAuthorize("hasPermission('/producer/test/page', 'POST')")
    fun page(@RequestBody request: FilterRequest): Response {
        val page = testService.page(request)
        return pageResponse(page, "tests")
    }
}