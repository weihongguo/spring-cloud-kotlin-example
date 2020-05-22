package com.example.database.entity

import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 15:57
 **/
@Entity
data class Test(
    var status: Int = 0,
    var name: String = "",
    var sort: Short = 0
) : BaseEntity()