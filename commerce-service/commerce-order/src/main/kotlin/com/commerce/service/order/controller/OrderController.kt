package com.commerce.service.order.controller

import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.config.ApiPaths
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiPaths.ORDERS)
class OrderController (
    private val orderUseCase: OrderUseCase
) {
    @GetMapping
    fun getOrder() {
        return orderUseCase.getOrder()
    }

    @GetMapping("/{orderId}")
    fun getOrderDetail() {
        return orderUseCase.getOrderDetail()
    }
}