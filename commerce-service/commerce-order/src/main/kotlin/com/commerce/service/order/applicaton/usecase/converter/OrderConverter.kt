package com.commerce.service.order.applicaton.usecase.converter

import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.Orders
import com.commerce.service.order.controller.response.OrderSummary

fun Orders.toOrderSummary(): OrderSummary {
    return OrderSummary(
        id = this.id.toString(),
        orderNumber = "${this.orderDate}-${this.memberId}", // 주문번호 = 주문 일자 + 주문자 ID (임시)
        content = this.content,
        orderDate = this.orderDate.toString(),
        status = this.status.toString(),
        pricie = this.price.toDouble(),
        discoutedPrice = this.discountedPrice.toDouble(),
        memberName = "Customer-${this.memberId}", // 임시 처리
        recipient = this.recipient
    )
}

fun Orders.toOrder(): Orders {
    return Orders(
        id = this.id,
        memberId = this.memberId,
        streetAddress = this.streetAddress,
        detailAddress = this.detailAddress,
        postalCode = this.postalCode,
        orderNumber = this.orderNumber,
        paymentMethod = this.paymentMethod,
        recipient = this.recipient,
        content = this.content,
        discountedPrice = this.discountedPrice,
        price = this.price,
        status = OrderStatus.valueOf(this.status.name),
        orderDate = this.orderDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        orderProducts = this.orderProducts.map { it.toOrderProducts() }
    )
}