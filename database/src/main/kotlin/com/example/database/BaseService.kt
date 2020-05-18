package com.example.database

import com.example.database.entity.BaseEntity
import com.example.database.entity.EntityNotFoundException
import com.example.database.entity.EntityOperateException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.util.*

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
        if (ids.isEmpty()) {
            return null
        }
        return getRepository().findByIdIn(ids)
    }
}

@NoRepositoryBean
interface BaseReadRepository<T : BaseEntity> : Repository<T, Long>, JpaSpecificationExecutor<T> {
    fun findById(id: Long): T?
    fun findByIdIn(ids: Collection<Long>): List<T>?
}

/* */

interface BaseService<T : BaseEntity> {
    fun getRepository(): BaseRepository<T>

    fun getById(id: Long): T?
    fun getByIdWithDeleted(id: Long): T?

    fun save(entity: T): T?

    fun update(entity: T): T?
    fun updateDirect(entity: T): T?

    fun remove(id: Long): T?
    fun restore(id: Long): T?
    fun destroy(id: Long)
}

abstract class BaseServiceImpl<T : BaseEntity> : BaseService<T> {

    override fun getById(id: Long): T? {
        val entity = getRepository().findById(id).orElse(null)
        if (null == entity || null != entity.deleteTime ) {
            return null
        }
        return entity
    }

    override fun getByIdWithDeleted(id: Long): T? {
        return getRepository().findById(id).orElse(null)
    }

    override fun save(entity: T): T? {
        setForCreate(entity)
        return getRepository().save(entity)
    }

    override fun update(entity: T): T? {
        val dbEntity = getById(entity.id!!) ?: throw EntityNotFoundException()
        setForUpdate(dbEntity, entity)
        return getRepository().save(dbEntity)
    }

    override fun updateDirect(entity: T): T? {
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
            throw EntityOperateException("资源创建失败")
        }
        val now = Date()
        entity.createTime = now
        entity.updateTime = now
        entity.deleteTime = null
    }

    open fun setForUpdate(entity: T) {
        entity.id?.let {
            entity.updateTime = Date()
        } ?: throw EntityOperateException("资源更新失败")
    }

    private fun setForUpdate(dbEntity: T, entity: T) {
        dbEntity.id?.let {
            dbEntity.updateTime = Date()
        } ?: throw EntityOperateException("资源更新失败")
    }

    private fun setForRemove(entity: T) {
        entity.id?.let {
            entity.deleteTime = Date()
        } ?: throw EntityOperateException("资源不可删除")
    }

    private fun setForRestore(entity: T) {
        entity.id?.let {
            entity.deleteTime = null
        } ?: throw EntityOperateException("资源不可恢复")
    }
}

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>
