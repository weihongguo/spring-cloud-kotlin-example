package com.example.database

import com.example.database.entity.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

interface BaseReadService<T : BaseEntity> {
    fun getRepository(): BaseReadRepository<T>

    fun getById(id: Long): T?
    fun listByIdIn(ids: Collection<Long>): List<T>?
}

@NoRepositoryBean
interface BaseReadRepository<T : BaseEntity> :
    Repository<T, Long>,
    JpaSpecificationExecutor<T>
{
    fun findById(id: Long): T?
    fun findByIdIn(ids: Collection<Long>): List<T>?
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
        if (ids.isEmpty()) {
            return null
        }
        return getRepository().findByIdIn(ids)
    }
}

interface BaseService<T : BaseEntity> {
    fun getRepository(): BaseRepository<T>
}

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> :
    JpaRepository<T, Long>,
    JpaSpecificationExecutor<T>

abstract class BaseServiceImpl<T : BaseEntity> : BaseService<T>

