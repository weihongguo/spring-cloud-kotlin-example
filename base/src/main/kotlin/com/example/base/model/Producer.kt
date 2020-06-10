package com.example.base.model

import javax.persistence.Entity

/**
 * @Author：GuoGuo
 * @Date 2020/6/10 16:46
 **/

@Entity
data class Producer(
    var name: String = ""
) : Model()