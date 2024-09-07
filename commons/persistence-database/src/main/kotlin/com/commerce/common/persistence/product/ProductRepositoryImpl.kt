package com.commerce.common.persistence.product

import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.persistence.category.CategoryJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl (
    private val productJpaRepository: ProductJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
) : ProductRepository {

    override fun findByCategoryId(categoryId: Long, page: Int, size: Int): List<Product> {

        return productJpaRepository.findByCategoryId(categoryId, PageRequest.of(page, size))
            .map { product ->
                val category = product.categoryId?.let { categoryId ->
                    categoryJpaRepository.findById(categoryId)
                        .map{ it.toProductModel() }
                        .orElse(null)
                }
                product.toModel(category)
            }
            .toList()
    }
}