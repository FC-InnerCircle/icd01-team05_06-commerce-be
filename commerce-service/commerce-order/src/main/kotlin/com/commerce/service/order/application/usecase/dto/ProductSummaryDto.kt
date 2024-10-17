package com.commerce.service.order.application.usecase.dto

import java.math.BigDecimal

data class ProductSummaryDto(
    val id: Long,
    val name: String,
    val quantity: Int,
    val price: BigDecimal,
    val discountedPrice: BigDecimal
)
