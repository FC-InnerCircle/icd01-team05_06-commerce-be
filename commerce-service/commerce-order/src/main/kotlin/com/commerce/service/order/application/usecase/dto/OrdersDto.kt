package com.commerce.service.order.application.usecase.dto

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.product.Product
import com.commerce.common.model.orders.OrderNumber
import java.math.BigDecimal

// 주문정보, 배송정보, 결제정보 포함된 주문 결과 dto
data class OrdersDto(
    val member: Member, // 주문자
    val customer: String, // recipient
    val orderNumber: OrderNumber,
    val products: List<OrderProduct>,
    val deliveryInfo: DeliveryInfo, // 배송정보
    val paymentInfo: PaymentInfo, // 결제정보
    val ordersInfo: OrdersInfo // 주문 정보
) {
    data class OrderProduct(
        val product: Product,
        val quantity: Int
    )

    data class DeliveryInfo(
        val recipient: String,
        val streetAddress: String,
        val detailAddress: String,
        val postalCode: String
    )

    data class PaymentInfo(
        val method: String,
        val totalAmount: BigDecimal,
        val depositorName: String
    )

    data class OrdersInfo(
        val content: String, // 주문 내용
        var OrderStatus: OrderStatus, // 주문 상태
    )

    // 주문상태 변경
    fun changeOrderStatus(status: OrderStatus) {
        ordersInfo.OrderStatus = status
    }
}