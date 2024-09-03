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
    // 데이터 설명
    // 주문 상태 (확정) - 24.09.03 프론트팀 공유 완료 (목데이터 준비)
    // PENDING: 주문 대기
    // PROCESSING: 처리 중
    // SHIPPED: 배송 중
    // DELIVERED: 배송 완료
    // CANCELLED: 주문 취소
    // REFUND: 환불
    // EXCHANGE: 교환

    // 데이터 추가 24.09.03 - 단비님
    // 고도화 예정: 부분취소 파트.
    // 환불, 교환, 취소
    // REFUND: 환불
    // EXCHANGE: 교환
    // CANCEL: 취소
    enum class OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    val totalAmount: Double
        get() = orderItems.sumOf { it.priceAtPurchase * it.quantity }
}