package com.commerce.service.product.application.service

import com.commerce.common.model.category.CategoryRepository
import com.commerce.common.model.product.ProductRepository
import com.commerce.service.product.application.usecase.ProductUseCase
import com.commerce.service.product.application.usecase.dto.PaginationInfoDto
import com.commerce.service.product.application.usecase.dto.ProductCategoryInfoDto
import com.commerce.service.product.application.usecase.dto.ProductInfoDto
import com.commerce.service.product.application.usecase.dto.ProductPaginationInfoDto
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

        return ProductCategoryInfoDto.from(categories)
    }

    // 검색 조건 공통 or 단건
    @Transactional(readOnly = true)
    override fun getProducts(query: SelectQuery): ProductPaginationInfoDto {

        val products = productRepository.findBySearchWord(
            searchWord = query.searchWord,
            categoryId = query.categoryId,
            homeProductType = query.homeProductType,
            page = query.page,
            size = query.size,
        )

        return ProductPaginationInfoDto(
            products = products.data.map { ProductInfoDto.from(it) },
            pagination = PaginationInfoDto.from(products.pagination)
        )
    }

    @Transactional(readOnly = true)
    override fun getProductDetail(productId: Long): ProductInfoDto {
        val product = productRepository.findById(productId);

        return ProductInfoDto.from(product)
    }
}