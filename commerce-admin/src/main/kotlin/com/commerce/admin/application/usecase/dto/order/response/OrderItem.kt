package com.commerce.admin.application.usecase.dto.order.response

data class OrderItem(
    val productId: String,
    val name: String,
    val quantity: Int,
    val price: Double,
)
