package com.example.elasticsearch.document

import org.springframework.data.annotation.Id

abstract class BaseDoc (
    @Id
    var id: String? = null
)