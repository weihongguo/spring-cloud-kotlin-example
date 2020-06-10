package com.example.base

import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.util.*
import javax.persistence.EntityNotFoundException

interface BaseReadService<T : BaseModel> {
    fun getRepository(): BaseReadRepository<T>

    fun getById(id: Long): T?
    fun listByIdIn(ids: Set<Long>): List<T>?
}

abstract class BaseReadServiceImpl<T : BaseModel> : BaseReadService<T> {

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

    override fun listByIdIn(ids: Set<Long>): List<T>? {
        if (ids.isEmpty()) {
            return null
        }
        return getRepository().findByIdIn(ids)
    }
}

@NoRepositoryBean
interface BaseReadRepository<T : BaseModel> : Repository<T, Long>, JpaSpecificationExecutor<T> {
    fun findById(id: Long): T?
    fun findByIdIn(ids: Set<Long>): List<T>?
}

/* */

interface BaseService<T : BaseModel> {
    fun getRepository(): BaseRepository<T>

    fun getFilterConfig(): FilterConfig?

    fun getById(id: Long): T?
    fun getByIdWithDeleted(id: Long): T?

    fun page(request: FilterRequest): Page<T>

    fun save(entity: T): T
    fun saveAll(entities: Iterable<T>): List<T>

    fun update(entity: T, direct: Boolean = false): T

    fun remove(id: Long): T?
    fun restore(id: Long): T?
    fun destroy(id: Long)
}

abstract class BaseServiceImpl<T : BaseModel> : BaseService<T> {

    override fun getFilterConfig(): FilterConfig? {
        return null
    }

    override fun getById(id: Long): T? {
        val entity = getRepository().findById(id).orElse(null)
        entity?.let {
            if (null == it.deleteTime) {
                return it
            }
        }
        return null
    }

    override fun getByIdWithDeleted(id: Long): T? {
        return getRepository().findById(id).orElse(null)
    }

    override fun page(request: FilterRequest): Page<T> {
        val specification = request.specification<T>(getFilterConfig())
        val pageRequest = request.pageRequest()
        return getRepository().findAll(specification, pageRequest)
    }

    override fun save(entity: T): T {
        setForCreate(entity)
        return getRepository().save(entity)
    }

    override fun saveAll(entities: Iterable<T>): List<T> {
        for (entity in entities) {
            setForCreate(entity)
        }
        return getRepository().saveAll(entities)
    }

    override fun update(entity: T, direct: Boolean): T {
        if (direct) {
            return updateDirect(entity)
        }

        entity.id?.let {
            val dbEntity = getById(it) ?: throw EntityNotFoundException()
            setForUpdate(dbEntity, entity)
            return getRepository().save(dbEntity)
        }
        throw EntityNotFoundException()
    }

    private fun updateDirect(entity: T): T {
        setForUpdate(entity)
        return getRepository().save(entity)
    }

    override fun remove(id: Long): T? {
        val dbEntity = getById(id) ?: throw EntityNotFoundException()
        setForRemove(dbEntity)
        return getRepository().save(dbEntity)
    }

    override fun restore(id: Long): T? {
        val dbEntity = getByIdWithDeleted(id) ?: throw EntityNotFoundException()
        setForRestore(dbEntity)
        return getRepository().save(dbEntity)
    }

    override fun destroy(id: Long) {
        getRepository().deleteById(id)
    }

    open fun setForCreate(entity: T) {
        entity.id?.let {
            throw ModelOperateException("资源创建失败")
        }
        val now = Date()
        entity.createTime = now
        entity.updateTime = now
        entity.deleteTime = null
    }

    open fun setForUpdate(dbEntity: T, entity: T) {
        dbEntity.id?.let { dbId ->
            entity.id?.let {
                if (it == dbId) {
                    dbEntity.updateTime = Date()
                    return
                }
            }
        }
        throw ModelOperateException("资源更新失败")
    }

    private fun setForUpdate(entity: T) {
        entity.id?.let {
            entity.updateTime = Date()
            return
        }
        throw ModelOperateException("资源更新失败")
    }

    private fun setForRemove(entity: T) {
        entity.id?.let {
            entity.deleteTime = Date()
        } ?: throw ModelOperateException("资源不可删除")
    }

    private fun setForRestore(entity: T) {
        entity.id?.let {
            entity.deleteTime = null
        } ?: throw ModelOperateException("资源不可恢复")
    }
}

@NoRepositoryBean
interface BaseRepository<T : BaseModel> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>
