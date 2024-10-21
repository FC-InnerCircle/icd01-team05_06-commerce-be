package com.commerce.common.persistence.product_tag

import org.springframework.data.jpa.repository.JpaRepository

interface ProductTagJpaRepository : JpaRepository<ProductTagJpaEntity, Long> {

    fun findByProductId(productId: Long): List<ProductTagJpaEntity>

    fun findByProductIdIn(productIds: List<Long>): List<ProductTagJpaEntity>
}