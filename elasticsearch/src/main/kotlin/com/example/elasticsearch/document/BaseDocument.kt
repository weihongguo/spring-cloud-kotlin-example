package com.example.elasticsearch.document

import org.springframework.data.annotation.Id

abstract class BaseDocument (
    @Id
    var id: String? = null
) {
    abstract fun index(): String
}

data class GeoPoint (
    var lon: Float,
    var lat: Float
) {
    override fun toString(): String = "[${lon},${lat}]"
}