package com.commerce.service.product.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.review.Review
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.model.review.ReviewWithProduct
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
) : ReviewUseCase {

    @Transactional(readOnly = true)
    override fun getProductReviews(productId: Long): List<ReviewWithMember> {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId)
    }

//    @Transactional
    override fun addReviewToProduct(member: Member?, addReviewCommand: AddReviewCommand?): Long {

        val memberIds: List<Long> = listOf(1, 2, 3, 4, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16, 21, 22, 23, 24)
        val contents: Map<Long, String> = mapOf(
            1L to "진짜 재미없어요 ㅠㅠ 최악이네요\n다시는 안볼 것 같습니다.",
            2L to "별로 재미 없습니다.\n시간이 남으시면 봐도 괜찮을 것 같아요.",
            3L to "그저 그래요.\n무난한 책입니다.",
            4L to "재미있게 잘 보았습니다.\n감사합니다.",
            5L to "정말 재미있어요!!\n인생 책인 것 같아요~~~",
        )

        memberIds.forEach { memberId ->
            for (i in 1L..100) {
                val score = (i + memberId) % 5 + 1
                val review = Review.byProduct(
                    productId = i,
                    memberId = memberId,
                    content = "${contents[score]}",
                    score = BigDecimal(score),
                    lastModifiedByUserAt = LocalDateTime.now(),
                )

                reviewRepository.save(review).id
            }
        }
        return 1
    }

    override fun getMemberReviews(memberId: Long): List<ReviewWithProduct> {
        return reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
    }
}