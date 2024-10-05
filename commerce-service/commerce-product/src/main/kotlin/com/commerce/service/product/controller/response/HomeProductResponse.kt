package com.commerce.service.product.controller.response

data class HomeProductResponse(
    val hotNew: List<HomeProductDto>,
    val recommend: List<HomeProductDto>,
    val bestseller: List<HomeProductDto>
)
