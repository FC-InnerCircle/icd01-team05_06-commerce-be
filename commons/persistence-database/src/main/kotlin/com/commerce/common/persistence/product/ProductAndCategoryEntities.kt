package com.commerce.common.persistence.product

import com.commerce.common.persistence.category.CategoryJpaEntity

data class ProductAndCategoryEntities(
    val productJpaEntity: ProductJpaEntity,
    val categoryJpaEntity: CategoryJpaEntity?
)