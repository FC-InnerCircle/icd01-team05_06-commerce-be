package com.commerce.service.order.application.usecase.command

data class PostShoppingCartCommand(
    val productId: Long,
    val quantity: Int
)
