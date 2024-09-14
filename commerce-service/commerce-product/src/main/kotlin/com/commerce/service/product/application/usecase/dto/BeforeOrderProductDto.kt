package com.commerce.service.product.application.usecase.dto

import java.math.BigDecimal

data class BeforeOrderProductDto(
    var productId: Long,
    var title: String,
    var coverImage: String,
    var quantity: Int,
    var price: BigDecimal,
    var discountedPrice: BigDecimal,
)