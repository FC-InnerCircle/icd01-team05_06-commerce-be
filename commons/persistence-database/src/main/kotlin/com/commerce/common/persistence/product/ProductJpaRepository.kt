package com.commerce.common.persistence.product

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductJpaEntity, Long>, KotlinJdslJpqlExecutor {
    fun findByCategoryId(categoryId: Long, pageable: Pageable): List<ProductJpaEntity>

    fun findByIdIn(ids: List<Long>): List<ProductJpaEntity>

    fun save(product: ProductJpaEntity): ProductJpaEntity
}