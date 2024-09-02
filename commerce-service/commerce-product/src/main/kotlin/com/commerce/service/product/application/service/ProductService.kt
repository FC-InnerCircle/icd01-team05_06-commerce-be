package com.commerce.service.product.application.service

import com.commerce.common.model.category.CategoryRepository
import com.commerce.common.model.product.ProductRepository
import com.commerce.service.product.application.usecase.ProductUseCase
import com.commerce.service.product.application.usecase.dto.ProductCategoryInfoDto
import com.commerce.service.product.application.usecase.dto.ProductInfoDto
import com.commerce.service.product.application.usecase.query.SelectQuery
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
) : ProductUseCase{

    @Transactional(readOnly = true)
    override fun getProductCategories(): ProductCategoryInfoDto {
        val categories = categoryRepository.findAll()

        return ProductCategoryInfoDto.of(categories)
    }

    @Transactional(readOnly = true)
    override fun getProducts(query: SelectQuery): List<ProductInfoDto> {
        val products = productRepository.findByCategoryId(query.categoryId, query.page, query.size)

        return products.map { ProductInfoDto.of(it) }
    }
}