package com.commerce.common.model.orderProduct

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderProduct(
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val quantity: Long,
    val price: BigDecimal,
    val discountedPrice: BigDecimal
)