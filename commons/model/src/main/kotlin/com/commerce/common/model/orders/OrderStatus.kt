package com.commerce.common.model.orders

// 주문상태
// PENDING: 주문 생성
// PROCESSING: 주문 처리중
// SHIPPED: 배송중
// DELIVERED: 배송완료
// CANCELLED: 주문 취소
// REFUND: 환불
// EXCHANGE: 교환
enum class OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUND,
    EXCHANGE
}