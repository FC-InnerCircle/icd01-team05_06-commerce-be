package com.commerce.service.order.application.usecase.dto

data class CreateOrderDto(
    val id: Long,
    val orderNumber: String,
    val orderStatus: String,
    val orderDate: String,
    val products: List<ProductSummaryDto>
)