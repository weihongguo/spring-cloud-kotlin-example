package com.example.base.consumer

import com.example.base.BaseModel
import javax.persistence.Entity

@Entity
data class Consumer(
        var name: String = "",
        var producerId: Long = -1
) : BaseModel()