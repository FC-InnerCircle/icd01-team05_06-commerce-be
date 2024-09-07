package com.commerce.service.order.controller.response

import java.time.LocalDateTime

data class OrderDetail(
    val id: String,
    val orderNumber: String,
    val orderDate: LocalDateTime,
    val status: String,
    val totalAmount: Double,
    val customerName: String,
    val shippingAddress: String,
    val paymentMethod: String
)