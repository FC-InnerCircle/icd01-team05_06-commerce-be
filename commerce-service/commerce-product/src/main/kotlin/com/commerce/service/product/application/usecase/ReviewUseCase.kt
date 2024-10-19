package com.commerce.service.product.application.usecase

import com.commerce.common.model.member.Member
import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.model.review.ReviewWithProduct
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import com.commerce.service.product.application.usecase.command.UpdateReviewCommand

interface ReviewUseCase {
    fun getProductReviews(productId: Long): List<ReviewWithMember>
    fun addReviewToProduct(member: Member, addReviewCommand: AddReviewCommand): Long
    fun updateReview(member: Member, reviewId: Long, command: UpdateReviewCommand)
    fun deleteReview(member: Member, reviewId: Long)
    fun getMemberReviews(memberId: Long): List<ReviewWithProduct>
}