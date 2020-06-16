package com.example.elasticsearch.controller

import com.example.base.RequestException
import com.example.base.Response
import com.example.base.okResponse
import com.example.elasticsearch.document.LbsDocument
import com.example.elasticsearch.document.LbsDocument.Companion.LBS_INDEX_NAME
import com.example.elasticsearch.document.LbsDocumentFilterRequest
import com.example.elasticsearch.service.DistanceRequest
import com.example.elasticsearch.service.LbsDocumentService
import com.example.elasticsearch.service.pageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @Author：GuoGuo
 * @Date 2020/6/15 17:41
 **/
@RestController
@RequestMapping("lbs")
class LbsController {

    @Autowired
    lateinit var lbsDocumentService: LbsDocumentService

    @PostMapping
    fun store(@RequestBody lbsDocument: LbsDocument): Response {
        val savedLbsDocument = lbsDocumentService.save(lbsDocument)
        return okResponse(mapOf(
                "lbsDocument" to savedLbsDocument
        ))
    }

    @GetMapping("{id}")
    fun show(@PathVariable id: Long): Response {
        val lbsDocument = lbsDocumentService.getById(LBS_INDEX_NAME, id.toString(), LbsDocument::class.java)
        return okResponse(mapOf(
            "id" to id,
            "lbsDocument" to lbsDocument
        ))
    }

    @PostMapping("page")
    fun page(@RequestBody filterRequest: LbsDocumentFilterRequest): Response {
        if (!filterRequest.check()) {
            throw RequestException()
        }
        val page = lbsDocumentService.page(LBS_INDEX_NAME, filterRequest, LbsDocument::class.java)
        return pageResponse(page, "lbsDocuments", mapOf(
            "test" to "test"
        ))
    }

    @PostMapping("search_by_distance")
    fun searchByDistance(@RequestBody distanceRequest: DistanceRequest): Response {
        if (!distanceRequest.check()) {
            throw RequestException()
        }
        val list = lbsDocumentService.searchByDistance(distanceRequest)
        return okResponse(mapOf(
                "lbsDocuments" to list
        ))
    }

    @PostMapping("script_sort")
    fun scriptSort(@RequestBody distanceRequest: DistanceRequest): Response {
        if (!distanceRequest.check()) {
            throw RequestException()
        }
        val list = lbsDocumentService.scriptSort(distanceRequest)
        return okResponse(mapOf(
                "lbsDocuments" to list
        ))
    }
}