package com.commerce.service.product.application.usecase.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class ReviewInfo(
    val reviewId: Long,
    val content: String,
    val score: BigDecimal,
    val memberId: String,
    val productId: Long,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    val updatedAt: LocalDateTime,
    val orderProductId: Long?,
)