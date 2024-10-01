package com.commerce.service.product.controller.response

import com.commerce.service.product.application.usecase.dto.ProductInfoDto
import com.commerce.service.product.application.usecase.dto.ReviewInfo

data class ProductDetailInfoDto(
    private val product: ProductInfoDto,
    private val review: ReviewInfo,
)