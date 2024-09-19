package com.commerce.service.product.application.usecase.dto

data class ProductPaginationInfoDto(
    val products: List<ProductInfoDto>,
    val pagination: PaginationInfoDto,
)