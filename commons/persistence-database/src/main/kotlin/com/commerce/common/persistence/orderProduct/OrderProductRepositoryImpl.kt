package com.commerce.common.persistence.orderProduct

import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.model.orderProduct.OrderProductRepository
import org.springframework.stereotype.Repository

@Repository
class OrderProductRepositoryImpl (
    private val orderProductJpaRepository: OrderProductJpaRepository
) : OrderProductRepository {
    override fun save(orderProduct: OrderProduct): OrderProduct {

        val ordersJpaEntity = OrdersJpaEntity(orderProduct.orderId)
        return orderProductJpaRepository.save(orderProduct.toJpaEntity(ordersJpaEntity)).toOrderProducts()
    }
}