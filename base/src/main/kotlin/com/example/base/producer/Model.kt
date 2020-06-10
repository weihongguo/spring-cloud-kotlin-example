package com.example.base.producer

import com.example.base.BaseModel
import javax.persistence.Entity

@Entity
data class Producer(
        var name: String = ""
) : BaseModel()