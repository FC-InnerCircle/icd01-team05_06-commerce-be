package com.commerce.common.model.review

import com.commerce.common.model.member.MemberRepository
import java.time.LocalDateTime

class FakeReviewRepository(
    private val memberRepository: MemberRepository,
) : ReviewRepository {

    var autoIncrementId = 1L
    var data: MutableList<Review> = mutableListOf()

    override fun findByProductId(productId: Long): List<ReviewWithMember> {
        return data.filter { it.productId == productId }
            .map {
                val member = memberRepository.findById(it.memberId)
                ReviewWithMember(
                    id = autoIncrementId++,
                    content = it.content,
                    score = it.score,
                    email = member!!.email,
                    productId = it.productId,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )
            }
    }

    override fun save(review: Review): Review {
        data.add(review)
        return data.last()
    }
}