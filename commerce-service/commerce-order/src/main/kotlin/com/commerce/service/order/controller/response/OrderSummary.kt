package com.commerce.service.order.controller.response

import com.commerce.common.model.orders.OrderNumber
import com.commerce.common.model.orders.OrderStatus
import com.commerce.service.order.application.usecase.dto.OrderSummaryDto

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
) {
    companion object {
        fun from(dto: OrderSummaryDto): OrderSummary {
            return OrderSummary(
                id = dto.id,
                orderNumber = dto.orderNumber,
                content = dto.content,
                orderDate = dto.orderDate,
                status = dto.status,
                price = dto.price,
                discountedPrice = dto.discountedPrice,
                memberName = dto.memberName,
                recipient = dto.recipient
            )
        }
    }
}