package com.commerce.common.persistence.product

import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl (
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {

    override fun findByCategoryId(categoryId: Long, page: Int, size: Int): List<Product> {
        return productJpaRepository.findByCategoryId(categoryId, PageRequest.of(page, size))
            .map { it.toModel() }
            .toList()
    }
}