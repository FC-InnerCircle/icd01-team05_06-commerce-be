package com.commerce.common.model.order

data class Order(
    val id: String,
    val customerName: String,
    val totalAmount: Double,
    val status: String,
    val createdAt: String,
    val itemsCount: Int
)
