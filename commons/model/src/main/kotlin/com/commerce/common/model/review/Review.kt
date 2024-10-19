package com.commerce.common.model.review

import java.math.BigDecimal
import java.time.LocalDateTime

class Review(
    val id: Long = 0,
    var content: String,
    var score: BigDecimal,
    val memberId: Long,
    val productId: Long,
    var lastModifiedByUserAt: LocalDateTime,
) {
    fun update(score: BigDecimal, content: String) {
        this.score = score
        this.content = content
        this.lastModifiedByUserAt = LocalDateTime.now()
    }

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