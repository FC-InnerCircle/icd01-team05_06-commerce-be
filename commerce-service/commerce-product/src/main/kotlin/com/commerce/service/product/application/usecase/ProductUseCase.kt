package com.commerce.service.product.application.usecase

import com.commerce.service.product.application.usecase.dto.ProductCategoryInfoDto
import com.commerce.service.product.application.usecase.dto.ProductInfoDto
import com.commerce.service.product.application.usecase.dto.ProductPaginationInfoDto
import com.commerce.service.product.application.usecase.query.SelectQuery

interface ProductUseCase {
    fun getProductCategories(): ProductCategoryInfoDto
    fun getProducts(query: SelectQuery): ProductPaginationInfoDto
    fun getProductDetail(productId: Long): ProductInfoDto
}