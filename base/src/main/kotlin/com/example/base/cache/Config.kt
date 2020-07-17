package com.example.base.cache

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class CacheConfig() : CachingConfigurerSupport() {

    @Bean
    fun redisCacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
        val valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(JdkSerializationRedisSerializer())
        val redisCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(60 * 60))
                .serializeKeysWith(keySerializationPair)
                .serializeValuesWith(valueSerializationPair)
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfig)
                .build()
    }

    @Bean
    fun <T> redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, T> {
        val redisTemplate = RedisTemplate<String, T>()
        redisTemplate.setConnectionFactory(connectionFactory)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = JdkSerializationRedisSerializer()
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }
}