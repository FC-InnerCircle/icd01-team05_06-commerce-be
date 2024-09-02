package com.commerce.common.persistence.productCategory

import com.commerce.common.persistence.category.CategoryJpaEntity
import com.commerce.common.persistence.product.ProductJpaEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_category")
class ProductCategoryJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: ProductJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: CategoryJpaEntity,
) {
}