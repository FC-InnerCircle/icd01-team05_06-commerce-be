package com.commerce.common.persistence.orderItem

import com.commerce.common.model.orderItem.OrderItemRepository
import org.springframework.stereotype.Repository

@Repository
class OrderItemRepositoryImpl (
    private val orderItemJpaRepository: OrderItemJpaRepository
) : OrderItemRepository {
}