package com.commerce.common.model.review

import java.math.BigDecimal
import java.time.LocalDateTime

data class ReviewWithProduct(
    val id: Long,
    val content: String,
    val score: BigDecimal,
    val productId: Long,
    val productTitle: String,
    val productAuthor: String,
    val productPublisher: String,
    val productCoverImage: String,
    val createdAt: LocalDateTime,
    val lastModifiedByUserAt: LocalDateTime,
)