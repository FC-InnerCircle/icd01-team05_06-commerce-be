package com.commerce.common.model.shopping_cart

import java.time.LocalDateTime

class ShoppingCart(
    val id: Long = 0,
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun updateQuantity(quantity: Int) = ShoppingCart(
        id = this.id,
        memberId = this.memberId,
        productId = this.productId,
        quantity = quantity,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}