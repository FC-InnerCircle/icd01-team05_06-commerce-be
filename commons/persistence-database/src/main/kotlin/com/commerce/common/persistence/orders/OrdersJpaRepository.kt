package com.commerce.common.persistence.orders

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface OrdersJpaRepository : JpaRepository<OrdersJpaEntity, Long> {
    fun findByCreatedAtBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<OrdersJpaEntity>

    fun findByCreatedAtBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrdersJpaEntity.OrderStatus,
        pageable: Pageable
    ): Page<OrdersJpaEntity>
}