package com.commerce.common.model.orders

import com.commerce.common.model.orderProduct.OrderProductWithInfo

data class OrdersResult(
    val id: Long,
    val memberId: Long,
    val orderNumber: OrderNumber,
    val orderer: OrdererInfo, // 주문자
    val products: List<OrderProductWithInfo>,
    val deliveryInfo: DeliveryInfo, // 배송정보
    val paymentInfo: PaymentInfo, // 결제정보
    val orderStatus: OrderStatus // 주문 상태
)