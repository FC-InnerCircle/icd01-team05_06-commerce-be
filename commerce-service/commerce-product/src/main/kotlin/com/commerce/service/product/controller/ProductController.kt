package com.commerce.service.product.controller

import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.ProductUseCase
import com.commerce.service.product.application.usecase.dto.ProductCategoryInfoDto
import com.commerce.service.product.application.usecase.dto.ProductInfoDto
import com.commerce.service.product.controller.request.ProductQueryRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productUseCase: ProductUseCase,
) {

    // 카테고리 select
    @GetMapping("/categories")
    fun getProductCategories(): CommonResponse<ProductCategoryInfoDto> {
        return CommonResponse.ok(productUseCase.getProductCategories())
    }

    // 상품 select
    @GetMapping
    fun getProducts(request: ProductQueryRequest): CommonResponse<List<ProductInfoDto>> {
        return CommonResponse.ok(productUseCase.getProducts(request.toQuery()))
    }
}