package com.commerce.admin.application.usecase.dto

data class OrderDto(
    val id: String,
    val customerName: String,
    val totalAmount: Double,
    val status: String,
    val createdAt: String,
    val itemsCount: Int
)

data class OrderListResponse(
    val orders: List<OrderDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)