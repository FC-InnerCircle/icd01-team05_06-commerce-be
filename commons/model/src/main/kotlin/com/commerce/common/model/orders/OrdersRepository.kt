package com.commerce.common.model.orders

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface OrdersRepository {
    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime, pageable: Pageable): Page<Orders>
    fun findByCreatedAtBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: Orders.OrderStatus,
        pageable: Pageable
    ): Page<Orders>
}