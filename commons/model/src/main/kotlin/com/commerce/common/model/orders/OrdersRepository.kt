package com.commerce.common.model.orders

import com.commerce.common.model.util.PaginationModel
import java.time.LocalDateTime

interface OrdersRepository {
    fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus?,
        page: Int,
        size: Int,
        sortOption: OrderSortOption
    ): PaginationModel<Orders>
}