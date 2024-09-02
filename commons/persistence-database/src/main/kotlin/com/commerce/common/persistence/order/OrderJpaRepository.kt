package com.commerce.common.persistence.order

import com.commerce.common.model.order.Order
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, Long> {
    fun findByCreatedAtBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<OrderJpaEntity>

    fun findByCreatedAtBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderJpaEntity.OrderStatus,
        pageable: Pageable
    ): Page<OrderJpaEntity>
}