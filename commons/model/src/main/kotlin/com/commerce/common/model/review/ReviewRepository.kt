package com.commerce.common.model.review

interface ReviewRepository {

    fun findByProductId(productId: Long): List<ReviewWithMember>

    fun save(review: Review): Review

    fun findByMemberId(memberId: Long): List<ReviewWithProduct>
}