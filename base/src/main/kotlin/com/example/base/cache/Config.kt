package com.example.base.cache

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig() : CachingConfigurerSupport() {

    @Bean
    fun redisCacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(getRedisSerializer())
        val redisCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(60 * 60))
                .serializeValuesWith(serializationPair)
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfig)
                .build()
    }

    private fun getRedisSerializer(): RedisSerializer<Any> {
        return GenericFastJsonRedisSerializer()
    }
}