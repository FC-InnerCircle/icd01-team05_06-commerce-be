package com.commerce.service.order.application.usecase.component

import com.commerce.service.order.application.usecase.dto.OrdersDto

/**
 * PaymentHandler
 * - 결제 처리
 */
interface PaymentHandler {
    fun processPayment(order: OrdersDto)
}