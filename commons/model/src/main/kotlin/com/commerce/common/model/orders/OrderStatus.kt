package com.commerce.common.model.orders

// 주문상태
enum class OrderStatus(
    val title: String
) {
    COMPLETED("주문 완료"),
    CANCELLED("주문 취소"),
    SHIPPING("배송중"),
    DELIVERED("배송완료"),
    REFUNDED("환불"),
}