package com.commerce.service.order.application.usecase.dto

import com.commerce.common.model.orders.OrderNumber
import com.commerce.common.model.orders.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderSummaryDto(
    val id: Long,
    val orderNumber: OrderNumber,
    val content: String,
    val orderDate: String,
    val status: OrderStatus,
    val price: Double,
    val discountedPrice: Double,
    val memberName: String,
    val recipient: String
)