package com.example.elasticsearch.document

import org.springframework.data.annotation.Id

abstract class BaseDocument (
    @Id
    var id: String? = null
) {
    abstract fun index(): String
}