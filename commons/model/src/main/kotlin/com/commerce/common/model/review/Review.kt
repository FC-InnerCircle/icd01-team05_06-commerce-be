package com.commerce.common.model.review

import java.math.BigDecimal
import java.time.LocalDateTime

class Review(
    val id: Long = 0,
    val content: String,
    val score: BigDecimal,
    val memberId: Long,
    val productId: Long,
    val lastModifiedByUserAt: LocalDateTime,
) {
    companion object {
        fun byProduct(
            productId: Long,
            memberId: Long,
            content: String,
            score: BigDecimal,
            lastModifiedByUserAt: LocalDateTime,
        ) = Review(
            content = content,
            score = score,
            memberId = memberId,
            productId = productId,
            lastModifiedByUserAt = lastModifiedByUserAt,
        )
    }
}