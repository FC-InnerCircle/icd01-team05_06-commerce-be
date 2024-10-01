package com.commerce.common.persistence.review

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ReviewJpaRepository : JpaRepository<ReviewJpaEntity, Long>{
    fun findByProductId(productId: Long): Optional<List<ReviewJpaEntity>>
}