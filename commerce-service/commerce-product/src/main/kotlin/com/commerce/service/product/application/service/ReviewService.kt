package com.commerce.service.product.application.service

import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.review.ReviewRepository
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import com.commerce.service.product.application.usecase.dto.ReviewInfoDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository,
) : ReviewUseCase{

    @Transactional(readOnly = true)
    override fun getProductReviews(productId: Long): ReviewInfoDto {
        val reviews = reviewRepository.findByProductId(productId)

        return ReviewInfoDto.from(reviews)
    }

    @Transactional(readOnly = true)
    override fun addReviewToProduct(addReviewCommand: AddReviewCommand): Long {

        val member = memberRepository.findByEmail(addReviewCommand.memberId)
            ?: throw IllegalArgumentException("해당 아이디(${addReviewCommand.memberId})를 가진 유저가 존재하지 않습니다.")

        val reviewId = reviewRepository.addReviewToProduct(
            addReviewCommand.productId,
            member.id,
            addReviewCommand.content,
            addReviewCommand.score,
            addReviewCommand.orderProductId,
        )
        return reviewId;
    }
}