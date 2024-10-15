package com.commerce.service.product.application.usecase

import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.model.review.ReviewWithProduct
import com.commerce.service.product.application.usecase.command.AddReviewCommand

interface ReviewUseCase {
    fun getProductReviews(productId: Long): List<ReviewWithMember>
    fun addReviewToProduct(addReviewCommand: AddReviewCommand): Long
    fun getMemberReviews(memberId: Long): List<ReviewWithProduct>
}