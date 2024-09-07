package com.commerce.common.persistence.product

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductJpaEntity, Long> {
    fun findByCategoryId(categoryId: Long, pageable: Pageable): List<ProductJpaEntity>
}