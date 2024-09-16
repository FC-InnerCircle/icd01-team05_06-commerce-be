package com.commerce.service.product.controller

import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.ProductUseCase
import com.commerce.service.product.application.usecase.dto.ProductCategoryInfoDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val productUseCase: ProductUseCase,
){
    // 카테고리 select
    @GetMapping
    fun getProductCategories(): CommonResponse<ProductCategoryInfoDto> {
        return CommonResponse.ok(productUseCase.getProductCategories())
    }
}