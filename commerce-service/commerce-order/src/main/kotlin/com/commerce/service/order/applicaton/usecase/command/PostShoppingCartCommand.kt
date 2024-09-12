package com.commerce.service.order.applicaton.usecase.command

data class PostShoppingCartCommand(
    val productId: Long,
    val quantity: Int
)
