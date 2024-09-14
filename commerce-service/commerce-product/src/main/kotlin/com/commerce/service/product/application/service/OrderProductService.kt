package com.commerce.service.product.application.service

import com.commerce.common.model.product.ProductRepository
import com.commerce.service.product.application.usecase.OrderProductUseCase
import com.commerce.service.product.application.usecase.dto.BeforeOrderProductDto
import com.commerce.service.product.application.usecase.query.BeforeOrderQuery
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderProductService(
    private val productRepository: ProductRepository
) : OrderProductUseCase {

    override fun getBeforeOrderProducts(query: BeforeOrderQuery): List<BeforeOrderProductDto> {
        val quantityMap = query.products.associate {
            it.productId to it.quantity
        }
        return productRepository.findByProductIdIn(query.products.map { it.productId }).map {
            val quantity = quantityMap[it.id]!!
            BeforeOrderProductDto(
                productId = it.id!!,
                title = it.title,
                coverImage = it.coverImage,
                quantity = quantity,
                price = it.price.multiply(BigDecimal(quantity)),
                discountedPrice = it.discountedPrice.multiply(BigDecimal(quantity))
            )
        }.toList()
    }
}