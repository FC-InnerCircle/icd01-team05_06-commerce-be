package com.commerce.common.persistence.review

import org.springframework.data.jpa.repository.JpaRepository

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long> {

    fun findByIdAndMemberId(id: Long, memberId: Long): ReviewJpaEntity?

    fun deleteByIdAndMemberId(id: Long, memberId: Long)
}