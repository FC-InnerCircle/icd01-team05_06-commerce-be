package com.commerce.common.persistence.order

import com.commerce.common.model.order.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl (
    private val orderJpaRepository: OrderJpaRepository
) : OrderRepository {

}