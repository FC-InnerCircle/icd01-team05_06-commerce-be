package com.commerce.service.order.controller.response

import java.time.LocalDateTime

data class OrderSummary(
    val id: String,
    val orderDate: LocalDateTime,
    val status: String,
    val totalAmount: Double,
    val customerName: String
)