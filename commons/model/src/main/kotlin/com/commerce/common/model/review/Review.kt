package com.commerce.common.model.review

import java.math.BigDecimal
import java.time.LocalDateTime

class Review(
    val id: Long = 0,
    val content: String,
    val score: BigDecimal,
    val memberId: Long,
    val productId: Long,
    val orderProductId: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    companion object {
        fun byProduct(
            productId: Long,
            memberId: Long,
            content: String,
            score: BigDecimal,
            orderProductId: Long?
        ) = Review(
            content = content,
            score = score,
            memberId = memberId,
            productId = productId,
            orderProductId = orderProductId,
        )
    }
}