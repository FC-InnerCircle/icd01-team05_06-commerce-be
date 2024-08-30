package com.commerce.admin.application.usecase.dto.order.response

import java.time.LocalDateTime

data class OrderSummary(
    val id: String,
    val customerName: String,
    val totalAmount: Double,
    val status: String,
    val createdAt: LocalDateTime,
)
