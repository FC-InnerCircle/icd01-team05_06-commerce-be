package com.commerce.common.model.orders

import com.commerce.common.model.orderProduct.OrderProductWithInfo
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrdersResult(
    val id: Long,
    val memberId: Long,
    val orderNumber: OrderNumber,
    val price: BigDecimal,
    val discountedPrice: BigDecimal,
    val orderDate: LocalDateTime,
    val orderer: OrdererInfo, // 주문자
    val products: List<OrderProductWithInfo>,
    val deliveryInfo: DeliveryInfo, // 배송정보
    val paymentInfo: PaymentInfo, // 결제정보
    val orderStatus: OrderStatus // 주문 상태
)