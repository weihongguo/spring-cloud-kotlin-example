package com.example.elasticsearch.service

import com.example.elasticsearch.document.GeoPoint
import com.example.elasticsearch.document.LbsDocument
import com.example.elasticsearch.document.LbsDocument.Companion.LBS_INDEX_NAME
import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.common.unit.DistanceUnit
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.rest.RestStatus
import org.elasticsearch.script.Script
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.ScriptSortBuilder
import org.elasticsearch.search.sort.SortBuilders
import org.elasticsearch.search.sort.SortOrder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

data class DistanceRequest (
        var point: GeoPoint,
        var distance: String
) {
    fun check(): Boolean {
        return true
    }
}

@Service
class LbsDocumentService : DocumentService() {
    private val log = LoggerFactory.getLogger(javaClass)

    fun searchByDistance(distanceRequest: DistanceRequest): Iterable<LbsDocument>? {
        val sourceBuilder = SearchSourceBuilder()
                .query(
                        QueryBuilders.geoDistanceQuery("location")
                            .point(distanceRequest.point.lat!!, distanceRequest.point.lon!!)
                            .distance(distanceRequest.distance)
                )
                .sort(
                        SortBuilders.geoDistanceSort("location", distanceRequest.point.lat!!, distanceRequest.point.lon!!)
                                .order(SortOrder.ASC)
                                .unit(DistanceUnit.KILOMETERS)
                )
        val searchRequest = SearchRequest(LBS_INDEX_NAME).source(sourceBuilder)
        val searchResponse =  client.search(searchRequest, RequestOptions.DEFAULT)
        if (searchResponse.status() == RestStatus.OK) {
            val hits = searchResponse.hits
            val list: MutableList<LbsDocument> = mutableListOf()
            hits.forEach {
                list.add(ObjectMapper().readValue(it.sourceAsString, LbsDocument::class.java))
            }
            return list
        }
        throw RuntimeException(searchResponse.status().toString())
    }

    fun scriptSort(distanceRequest: DistanceRequest): Iterable<LbsDocument>? {
        val sourceBuilder = SearchSourceBuilder()
                .query(
                        QueryBuilders.geoDistanceQuery("location")
                                .point(distanceRequest.point.lat!!, distanceRequest.point.lon!!)
                                .distance(distanceRequest.distance)
                )
                .sort(
                        SortBuilders.scriptSort(
                                Script("doc['location'].value.getLat() + doc['location'].value.getLon()"),
                                ScriptSortBuilder.ScriptSortType.NUMBER
                        ).order(SortOrder.ASC)
                )
        val searchRequest = SearchRequest(LBS_INDEX_NAME).source(sourceBuilder)
        val searchResponse =  client.search(searchRequest, RequestOptions.DEFAULT)
        if (searchResponse.status() == RestStatus.OK) {
            val hits = searchResponse.hits
            val list: MutableList<LbsDocument> = mutableListOf()
            hits.forEach {
                list.add(ObjectMapper().readValue(it.sourceAsString, LbsDocument::class.java))
            }
            return list
        }
        throw RuntimeException(searchResponse.status().toString())
    }
}