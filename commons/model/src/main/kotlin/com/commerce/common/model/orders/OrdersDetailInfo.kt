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
    val ordersInfo: OrdersInfo // 주문 정보
) {

    data class OrdersInfo(
        val content: String, // 주문 내용
        var orderStatus: OrderStatus, // 주문 상태
    )

    // 주문상태 변경
    fun changeOrderStatus(status: OrderStatus) {
        ordersInfo.orderStatus = status
    }
}