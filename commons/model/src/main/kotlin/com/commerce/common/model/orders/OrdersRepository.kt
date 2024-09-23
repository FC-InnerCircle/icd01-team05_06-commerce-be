package com.commerce.common.model.orders

import com.commerce.common.model.util.PaginationModel
import java.time.LocalDate

interface OrdersRepository {
    fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDate,
        endDate: LocalDate,
        status: OrderStatus?,
        page: Int,
        size: Int,
        sortOption: OrderSortOption
    ): PaginationModel<Orders>
}