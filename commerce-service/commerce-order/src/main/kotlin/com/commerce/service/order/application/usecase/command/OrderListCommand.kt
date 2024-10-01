package com.commerce.service.order.application.usecase.command

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrderSortOption
import com.commerce.common.model.orders.OrderStatus
import java.time.LocalDate

data class OrderListCommand(
    val member: Member,
    val orderDate: LocalDate,
    val endDate: LocalDate,
    val status: OrderStatus?,
    val sortBy: OrderSortOption,
    val page: Int,
    val size: Int
)
