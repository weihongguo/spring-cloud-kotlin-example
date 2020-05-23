package com.example.database.entity

import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 15:57
 **/
@Entity
data class Test(
    var intValue: Int = 0,
    var stingValue: String = ""
) : BaseEntity()