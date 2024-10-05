package com.commerce.service.product.controller.response

data class ReviewCreateResponse(
    val reviewId: Long,
) {
    companion object {
        fun of(reviewId: Long): ReviewCreateResponse {
            return ReviewCreateResponse(
                reviewId = reviewId,
            )
        }
    }

}