package com.commerce.common.persistence.productCategory

import com.commerce.common.persistence.category.CategoryJpaEntity
import com.commerce.common.persistence.product.ProductJpaEntity
import jakarta.persistence.*
import java.time.LocalDateTime

// @Entity
@Table(name = "product_category")
class ProductCategoryJpaEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: ProductJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: CategoryJpaEntity,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,

    ) {
}