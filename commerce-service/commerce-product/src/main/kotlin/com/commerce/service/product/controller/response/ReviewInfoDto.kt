package com.commerce.service.product.controller.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class ReviewInfoDto(
    val reviewId: Long,
    val content: String,
    val score: BigDecimal,
    val email: String,
    val productId: Long,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    val updatedAt: LocalDateTime,
    val orderProductId: Long?,
)