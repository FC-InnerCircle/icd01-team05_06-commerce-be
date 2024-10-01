package com.commerce.service.product.application.usecase.dto

import com.commerce.common.model.review.Review

class ReviewInfoDto(
    val reviews: List<ReviewInfo>,
) {
    companion object {
        fun from(reviews: List<Review>): ReviewInfoDto {
            val reviewList = reviews.map { review ->
                ReviewInfo(
                    reviewId = review.id!!,
                    content = review.content,
                    score = review.score,
                    memberId = review.memberId,
                    productId = review.productId,
                    updatedAt = review.updatedAt,
                    orderProductId = review.orderProductId,
                )
            }
            return ReviewInfoDto(
                reviews = reviewList,
            )
        }
    }
}