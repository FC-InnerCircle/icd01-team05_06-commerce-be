package com.commerce.service.product.application.usecase

import com.commerce.common.model.product.HomeProductType
import com.commerce.common.model.product.Product

interface HomeProductUseCase {

    fun getHomeProducts(homeProductType: HomeProductType): List<Product>
}