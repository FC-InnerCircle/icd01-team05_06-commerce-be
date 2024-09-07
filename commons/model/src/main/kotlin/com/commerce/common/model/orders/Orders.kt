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
    val orderNumber: String,
    val paymentMethod: String,
    val recipient: String,
    val content: String,
    val discountedPrice: BigDecimal,
    val price: BigDecimal,
    val status: OrderStatus,
    val orderDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val orderProducts: List<OrderProduct>
) {
    // 주문상태
    // PENDING: 주문 생성
    // PROCESSING: 주문 처리중
    // SHIPPED: 배송중
    // DELIVERED: 배송완료
    // CANCEL: 주문 취소
    // REFUND: 환불
    // EXCHANGE: 교환
    enum class OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCEL, REFUND, EXCHANGE
    }

    val totalAmount: BigDecimal
        get() = orderProducts.sumOf { it.price * BigDecimal(it.quantity) }
}