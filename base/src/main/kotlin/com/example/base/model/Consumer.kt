package com.example.base.model

import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/6/10 16:47
 **/

@Entity
data class Consumer(
    var name: String = "",
    var producerId: Long = -1
) : Model()