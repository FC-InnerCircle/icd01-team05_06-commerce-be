package com.commerce.service.order.applicaton.service

import com.commerce.common.model.order.OrderRepository
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import org.springframework.stereotype.Service

@Service
class OrderService (
    private val OrderRepository: OrderRepository
) : OrderUseCase {
    override fun getOrder() {
        return
    }

    override fun getOrderDetail() {
        return
    }
}