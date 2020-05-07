package com.example.database.repository.base

import com.example.database.domain.base.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

@NoRepositoryBean
interface BaseReadRepository<T : BaseEntity> :
    Repository<T, Long>,
    JpaSpecificationExecutor<T>
{
    fun findById(id: Long): T?
    fun findByIdIn(ids: Collection<Long>): List<T>?
}

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> :
    JpaRepository<T, Long>,
    JpaSpecificationExecutor<T>
