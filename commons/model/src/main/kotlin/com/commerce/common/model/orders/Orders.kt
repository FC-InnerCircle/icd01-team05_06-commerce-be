package com.commerce.common.model.orders

import com.commerce.common.model.orderProduct.OrderProduct
import java.math.BigDecimal
import java.time.LocalDateTime

data class Orders(
    val id: Long,
    val memberId: Long,
    val streetAddress: String,
    val detailAddress: String,
    val postalCode: String,
    val orderNumber: OrderNumber,
    val paymentMethod: String,
    val recipient: String,
    val content: String,
    val discountedPrice: BigDecimal,
    val price: BigDecimal,
    val status: OrderStatus,
    val orderDate: LocalDateTime,
    val orderProducts: List<OrderProduct>
) {

    val totalAmount: BigDecimal
        get() = orderProducts.sumOf { it.price * BigDecimal(it.quantity) }
}