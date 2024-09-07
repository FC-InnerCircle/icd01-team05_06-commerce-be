package com.commerce.common.persistence.orderProduct

import com.commerce.common.model.orderProduct.OrderProductRepository
import org.springframework.stereotype.Repository

@Repository
class OrderProductRepositoryImpl (
    private val orderProductJpaRepository: OrderProductJpaRepository
) : OrderProductRepository {
}