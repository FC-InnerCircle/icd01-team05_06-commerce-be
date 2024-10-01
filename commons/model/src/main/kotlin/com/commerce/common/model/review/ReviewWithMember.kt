package com.commerce.common.model.review

import java.math.BigDecimal
import java.time.LocalDateTime

data class ReviewWithMember(
    val id: Long,
    val content: String,
    val score: BigDecimal,
    val email: String,
    val productId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)