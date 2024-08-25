package com.commerce.common.persistence.order

import com.commerce.common.model.order.OrderRepository
import com.commerce.common.persistence.orderItem.OrderItemJpaEntity
import com.commerce.common.persistence.orderItem.OrderItemJpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class OrderRepositoryImpl (
    private val orderJpaRepository: OrderJpaRepository
) : OrderRepository  {

}