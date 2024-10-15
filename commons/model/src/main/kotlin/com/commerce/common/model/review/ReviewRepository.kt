package com.commerce.common.model.review

interface ReviewRepository {

    fun findByProductIdOrderByCreatedAtDesc(productId: Long): List<ReviewWithMember>

    fun save(review: Review): Review

    fun findByMemberIdOrderByCreatedAtDesc(memberId: Long): List<ReviewWithProduct>
}