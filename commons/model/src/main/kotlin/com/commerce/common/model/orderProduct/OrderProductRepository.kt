package com.commerce.common.model.orderProduct

interface OrderProductRepository {
    fun save(orderProduct: OrderProduct): OrderProduct
}