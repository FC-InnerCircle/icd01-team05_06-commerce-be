package com.commerce.common.model.orders

import com.commerce.common.model.product.ProductWithQuantity

// 주문정보, 배송정보, 결제정보 포함된 주문 결과
data class OrdersDetailInfo(
    val memberId: Long,
    val orderer: OrdererInfo, // 주문자
    val orderNumber: OrderNumber,
    val products: List<ProductWithQuantity>,
    val deliveryInfo: DeliveryInfo, // 배송정보
    val paymentInfo: PaymentInfo, // 결제정보
    val orderStatus: OrderStatus // 주문 상태
)