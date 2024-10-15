package com.commerce.service.product.application.service

import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.review.Review
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.model.review.ReviewWithProduct
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
) : ReviewUseCase{

    @Transactional(readOnly = true)
    override fun getProductReviews(productId: Long): List<ReviewWithMember> {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId)
    }

    @Transactional
    override fun addReviewToProduct(addReviewCommand: AddReviewCommand): Long {

        val member = memberRepository.findByEmail(addReviewCommand.email)
            ?: throw IllegalArgumentException("해당 아이디(${addReviewCommand.email})를 가진 유저가 존재하지 않습니다.")

        val review = Review.byProduct(
            productId = addReviewCommand.productId,
            memberId = member.id,
            content = addReviewCommand.content,
            score = addReviewCommand.score,
            lastModifiedByUserAt = LocalDateTime.now(),
        )

        return reviewRepository.save(review).id
    }

    override fun getMemberReviews(memberId: Long): List<ReviewWithProduct> {
        return reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
    }
}