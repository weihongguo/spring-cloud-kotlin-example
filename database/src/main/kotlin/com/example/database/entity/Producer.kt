package com.example.database.entity

import javax.persistence.Entity

@Entity
data class Producer(
    var name: String = ""
) : BaseEntity()