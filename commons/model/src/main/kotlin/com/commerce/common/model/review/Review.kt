package com.commerce.common.model.review

import java.math.BigDecimal
import java.time.LocalDateTime

class Review(
    val id: Long? = 0,
    val content: String,
    val score: BigDecimal,
    val memberId: String,
    val productId: Long,
    val orderProductId: Long? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)