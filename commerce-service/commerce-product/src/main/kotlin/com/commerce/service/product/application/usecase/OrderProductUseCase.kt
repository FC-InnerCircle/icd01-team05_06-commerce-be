package com.commerce.service.product.application.usecase

import com.commerce.service.product.application.usecase.dto.BeforeOrderProductDto
import com.commerce.service.product.application.usecase.query.BeforeOrderQuery

interface OrderProductUseCase {
    fun getBeforeOrderProducts(query: BeforeOrderQuery): List<BeforeOrderProductDto>
}