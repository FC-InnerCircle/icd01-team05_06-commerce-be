package com.commerce.service.product.application.service

import com.commerce.common.model.product.HomeProductType
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import com.commerce.service.product.application.usecase.HomeProductUseCase
import org.springframework.stereotype.Service

@Service
class HomeProductService(
    private val productRepository: ProductRepository
) : HomeProductUseCase {
    override fun getHomeProducts(homeProductType: HomeProductType): List<Product> {
        return productRepository.findByHomeProductType(homeProductType)
    }
}