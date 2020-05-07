package com.example.database.service.base

import com.example.database.domain.base.BaseEntity
import com.example.database.repository.base.BaseReadRepository
import com.example.database.repository.base.BaseRepository

interface BaseReadService<T : BaseEntity> {
    fun getRepository(): BaseReadRepository<T>

    fun getById(id: Long): T?
    fun listByIdIn(ids: Collection<Long>): List<T>?
}

abstract class BaseReadServiceImpl<T : BaseEntity> : BaseReadService<T> {

    override fun getById(id: Long): T? {
        if (id < 0) {
            return null
        }
        val entity = getRepository().findById(id)
        if (entity == null || entity.deleteTime != null) {
            return null
        }
        return entity
    }

    override fun listByIdIn(ids: Collection<Long>): List<T>? {
        if (ids == null || ids.isEmpty()) {
            return null
        }
        return getRepository().findByIdIn(ids)
    }
}

interface BaseService<T : BaseEntity> {
    fun getRepository(): BaseRepository<T>
}

abstract class BaseServiceImpl<T : BaseEntity> : BaseService<T>