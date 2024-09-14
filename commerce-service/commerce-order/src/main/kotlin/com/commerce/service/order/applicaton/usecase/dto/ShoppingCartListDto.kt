package com.commerce.service.order.applicaton.usecase.dto

import com.commerce.common.model.shopping_cart.ShoppingCartProduct

data class ShoppingCartListDto(
    val products: List<ShoppingCartProduct>
)