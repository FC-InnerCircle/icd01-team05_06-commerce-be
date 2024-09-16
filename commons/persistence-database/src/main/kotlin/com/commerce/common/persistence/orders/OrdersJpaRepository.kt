package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.OrderStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface OrdersJpaRepository : JpaRepository<OrdersJpaEntity, Long> {

    fun findByMemberIdAndCreatedAtBetween(
        memberId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<OrdersJpaEntity>

    fun findByMemberIdAndCreatedAtBetweenAndStatus(
        memberId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus,
        pageable: Pageable
    ): Page<OrdersJpaEntity>
}