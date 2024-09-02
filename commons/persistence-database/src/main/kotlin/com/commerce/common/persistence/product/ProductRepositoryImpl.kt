package com.commerce.common.persistence.product

import com.commerce.common.model.product.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl (
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
}