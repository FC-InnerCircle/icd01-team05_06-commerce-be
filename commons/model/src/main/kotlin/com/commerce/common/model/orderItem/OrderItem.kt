package com.commerce.common.model.orderItem

import java.time.LocalDateTime

data class OrderItem(
    val id: Long,
    val orderId: Long,
    val itemId: Long,
    val quantity: Int,
    val priceAtPurchase: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)