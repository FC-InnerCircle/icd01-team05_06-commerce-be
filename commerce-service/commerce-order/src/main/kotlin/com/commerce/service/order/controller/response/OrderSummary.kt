package com.commerce.service.order.controller.response

import com.commerce.common.model.orders.OrderNumber
import com.commerce.common.model.orders.OrderStatus

data class OrderSummary(
    val id: Long,
    val orderNumber: OrderNumber, // 주문 번호
    val content: String, // 주문 내역
    val orderDate: String, // 주문 일자
    val status: OrderStatus, // 주문 상태
    val price: Double, // 가격
    val discountedPrice: Double, // 할인된 가격
    val memberName: String, // 주문자 이름
    val recipient: String, // 수령인
)