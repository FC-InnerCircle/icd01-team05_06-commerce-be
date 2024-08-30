package com.commerce.admin.application.usecase.dto.order.response

data class OrderStatistics(
    val totalOrders: Int,
    val totalRevenue: Double,
    val averageOrderValue: Double,
)
