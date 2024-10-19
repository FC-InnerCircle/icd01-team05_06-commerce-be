package com.commerce.service.product.application.usecase.command

import java.math.BigDecimal

data class UpdateReviewCommand(
    val content: String,
    val score: BigDecimal,
)