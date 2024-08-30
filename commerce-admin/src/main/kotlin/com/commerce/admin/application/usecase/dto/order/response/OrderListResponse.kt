package com.commerce.admin.application.usecase.dto.order.response

data class OrderListResponse(
    val orders: List<OrderSummary>,
    val total: Int,
    val page: Int,
    val limit: Int,
)
