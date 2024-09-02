package com.commerce.common.persistence.product.image

import org.springframework.data.jpa.repository.JpaRepository

interface ProductImageJpaRepository : JpaRepository<ProductImageJpaEntity, Long> {
}