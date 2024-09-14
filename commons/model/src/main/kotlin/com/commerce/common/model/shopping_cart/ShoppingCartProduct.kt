package com.commerce.common.model.shopping_cart

import java.math.BigDecimal

data class ShoppingCartProduct(
    var shoppingCartId: Long,
    var productId: Long,
    var title: String,
    var coverImage: String,
    var quantity: Int,
    var price: BigDecimal,
    var discountedPrice: BigDecimal,
)