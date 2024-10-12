package com.commerce.service.product.controller.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class ReviewByMemberResponse(
    val reviews: List<ReviewByMemberDto>,
) {
    data class ReviewByMemberDto(
        val reviewId: Long,
        val content: String,
        val score: BigDecimal,
        val productId: Long,
        val productTitle: String,
        val productAuthor: String,
        val productPublisher: String,
        val productCoverImage: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
        val lastModifiedByUserAt: LocalDateTime,
    )
}