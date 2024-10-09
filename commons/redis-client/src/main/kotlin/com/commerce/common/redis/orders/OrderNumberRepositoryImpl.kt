package com.commerce.common.redis.orders

import com.commerce.common.model.orders.OrderNumberRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate

const val COUNT_KEY = "orders.orderNumber.orderCount"

@Repository
class OrderNumberRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : OrderNumberRepository {

    override fun countIncrementAndGet(date: LocalDate): Long {
        return redisTemplate.opsForValue().increment("$COUNT_KEY:$date")!!
    }
}