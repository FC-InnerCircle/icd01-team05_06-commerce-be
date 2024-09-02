package com.commerce.common.model.order

import com.commerce.common.model.orderItem.OrderItem
import java.time.LocalDateTime

data class Order(
    val id: Long,
    val memberId: Long,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val streetAddress: String,
    val detailAddress: String,
    val postalCode: String,
    val orderItems: List<OrderItem>
) {
    enum class OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    val totalAmount: Double
        get() = orderItems.sumOf { it.priceAtPurchase * it.quantity }
}