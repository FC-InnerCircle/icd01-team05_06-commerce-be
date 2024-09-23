package com.commerce.service.order.applicaton.usecase.converter

import com.commerce.common.model.orderProduct.OrderProduct

fun OrderProduct.toOrderProducts(): OrderProduct {
    return OrderProduct(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        quantity = this.quantity,
        price = this.price,
        discountedPrice = this.discountedPrice,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}