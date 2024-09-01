package com.commerce.service.order.controller.response

data class OrderListResponse(
    val orders: List<OrderSummary>,
    val totalElements: Long,
    val totalPages: Int
)