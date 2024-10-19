package com.commerce.service.product.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.review.Review
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.model.review.ReviewWithProduct
import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import com.commerce.service.product.application.usecase.command.UpdateReviewCommand
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
) : ReviewUseCase {

    @Transactional(readOnly = true)
    override fun getProductReviews(productId: Long): List<ReviewWithMember> {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId)
    }

    @Transactional
    override fun addReviewToProduct(member: Member, addReviewCommand: AddReviewCommand): Long {

        val review = Review.byProduct(
            productId = addReviewCommand.productId,
            memberId = member.id,
            content = addReviewCommand.content,
            score = addReviewCommand.score,
            lastModifiedByUserAt = LocalDateTime.now(),
        )

        return reviewRepository.save(review).id
    }

    override fun updateReview(member: Member, reviewId: Long, command: UpdateReviewCommand) {
        val review = reviewRepository.findByIdAndMember(reviewId, member)
            ?: throw CustomException(HttpStatus.BAD_REQUEST, ErrorCode.REVIEW_NOT_FOUND)

        review.update(
            score = command.score,
            content = command.content
        )
        reviewRepository.save(review)
    }

    override fun deleteReview(member: Member, reviewId: Long) {
        reviewRepository.deleteByIdAndMember(reviewId, member)
    }

    override fun getMemberReviews(memberId: Long): List<ReviewWithProduct> {
        return reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
    }
}