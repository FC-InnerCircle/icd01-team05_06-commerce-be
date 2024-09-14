package com.commerce.service.product.controller.response

import com.commerce.service.product.application.usecase.dto.BeforeOrderProductDto

data class BeforeOrderResponse(
    val products: List<BeforeOrderProductDto>
)