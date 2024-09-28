package com.commerce.common.model.shopping_cart

class ShoppingCart(
    val id: Long = 0,
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
) {
    fun updateQuantity(quantity: Int) = ShoppingCart(
        id = this.id,
        memberId = this.memberId,
        productId = this.productId,
        quantity = quantity,
    )
}