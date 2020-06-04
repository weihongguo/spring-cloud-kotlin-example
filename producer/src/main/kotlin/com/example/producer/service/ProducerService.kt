package com.example.producer.service

import com.alibaba.fastjson.parser.ParserConfig
import com.example.database.BaseRepository
import com.example.database.BaseService
import com.example.database.BaseServiceImpl
import com.example.database.entity.Producer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

/**
 * @Author：GuoGuo
 * @Date 2020/5/22 15:59
 **/
interface ProducerService : BaseService<Producer>

@Service
class ProducerServiceImpl : BaseServiceImpl<Producer>(), ProducerService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var producerRepository: ProducerRepository

    override fun getRepository(): BaseRepository<Producer> {
        return producerRepository
    }

    @Cacheable(cacheNames = ["producer::id"], key = "#id", unless="#result == null")
    override fun getById(id: Long): Producer? {
        return super.getById(id)
    }

    @CacheEvict(cacheNames = ["producer::id"], key = "#entity.id")
    override fun update(entity: Producer, direct: Boolean): Producer {
        return super.update(entity, direct)
    }

    override fun setForUpdate(dbEntity: Producer, entity: Producer) {
        super.setForUpdate(dbEntity, entity)
        dbEntity.name = entity.name
    }

    @CacheEvict(cacheNames = ["producer::id"], key = "#id")
    override fun remove(id: Long): Producer? {
        return super.remove(id)
    }

    @CacheEvict(cacheNames = ["producer::id"], key = "#id")
    override fun destroy(id: Long) {
        super.destroy(id)
    }
}

@Repository
interface ProducerRepository : BaseRepository<Producer>