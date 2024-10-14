package com.commerce.common.persistence.category

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryJpaRepository : JpaRepository<CategoryJpaEntity, Long> {
    fun findByIdIn(categoryIds: List<Long>): List<CategoryJpaEntity>
}