package com.commerce.common.model.review

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import java.time.LocalDateTime

class FakeReviewRepository(
    private val memberRepository: MemberRepository,
) : ReviewRepository {

    var autoIncrementId = 1L
    var data: MutableList<Review> = mutableListOf()

    override fun findByIdAndMember(id: Long, member: Member): Review? {
        return data.find { it.id == id && it.memberId == member.id }
    }

    override fun findByProductIdOrderByCreatedAtDesc(productId: Long): List<ReviewWithMember> {
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
                    lastModifiedByUserAt = LocalDateTime.now(),
                )
            }
    }

    override fun save(review: Review): Review {
        data.add(review)
        return data.last()
    }

    override fun findByMemberIdOrderByCreatedAtDesc(memberId: Long): List<ReviewWithProduct> {
        return data.filter { it.memberId == memberId }
            .map {
                ReviewWithProduct(
                    id = autoIncrementId++,
                    content = it.content,
                    score = it.score,
                    productId = it.productId,
                    productTitle = "상품${it.productId}",
                    productAuthor = "작성자${it.productId}",
                    productPublisher = "출판사${it.productId}",
                    productCoverImage = "https://image${it.productId}.com",
                    createdAt = LocalDateTime.now(),
                    lastModifiedByUserAt = LocalDateTime.now()
                )
            }
    }

    override fun deleteByIdAndMember(id: Long, member: Member) {
        data = data.filter { it.id != id || it.memberId != member.id }.toMutableList()
    }
}