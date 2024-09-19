package com.commerce.common.model.orders

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface OrdersRepository {
    fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus?,
        pageable: Pageable
    ): Page<Orders>
}