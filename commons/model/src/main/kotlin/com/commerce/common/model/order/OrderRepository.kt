package com.commerce.common.model.order

import com.commerce.common.persistence.order.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface OrderRepository {
    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime, pageable: Pageable): Page<Order>
    fun findByCreatedAtBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: Order.OrderStatus,
        pageable: Pageable
    ): Page<Order>
}