package com.commerce.admin.application.usecase.dto.order.response

import com.commerce.common.model.orderItem.OrderItem
import java.time.LocalDateTime

data class OrderDetails(
    val id: String,
    val customerName: String,
    val email: String,
    val totalAmount: Double,
    val status: String,
    val items: List<OrderItem>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
