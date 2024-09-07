package com.commerce.service.order.controller.response

data class OrderListResponse(
    val products: List<OrderSummary>,
    val totalElements: Long,
    val totalPages: Int
)