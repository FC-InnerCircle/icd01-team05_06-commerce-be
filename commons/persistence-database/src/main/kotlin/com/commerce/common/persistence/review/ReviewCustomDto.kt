package com.commerce.common.persistence.review

import com.commerce.common.model.review.Review
import java.math.BigDecimal
import java.time.LocalDateTime

data class ReviewCustomDto(
    private val id: Long,
    private val content: String,
    private val score: BigDecimal,
    private val email: String,
    private val productId: Long,
    private val createdAt: LocalDateTime,
    private val updatedAt: LocalDateTime,
) {
    fun toModel(): Review {
        return Review(
            id = id,
            content = content,
            score = score,
            memberId = email,
            productId = productId,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}