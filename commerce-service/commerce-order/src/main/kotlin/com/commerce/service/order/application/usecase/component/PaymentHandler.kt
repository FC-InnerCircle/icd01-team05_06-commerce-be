package com.commerce.service.order.application.usecase.component

import com.commerce.common.model.orders.OrdersDetailInfo

/**
 * PaymentHandler
 * - 결제 처리
 */
interface PaymentHandler {
    fun processPayment(order: OrdersDetailInfo)
}