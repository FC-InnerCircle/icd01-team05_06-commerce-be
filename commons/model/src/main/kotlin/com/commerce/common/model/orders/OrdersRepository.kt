package com.commerce.common.model.orders

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface OrdersRepository {
    fun findByMemberIdAndCreatedAtBetween(
        memberId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<Orders>

    fun findByMemberIdAndCreatedAtBetweenAndStatus(
        memberId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus,
        pageable: Pageable
    ): Page<Orders>
}