package com.commerce.common.model.review

import com.commerce.common.model.member.Member

interface ReviewRepository {

    fun findByIdAndMember(id: Long, member: Member): Review?

    fun findByProductIdOrderByCreatedAtDesc(productId: Long): List<ReviewWithMember>

    fun save(review: Review): Review

    fun findByMemberIdOrderByCreatedAtDesc(memberId: Long): List<ReviewWithProduct>

    fun deleteByIdAndMember(id: Long, member: Member)
}