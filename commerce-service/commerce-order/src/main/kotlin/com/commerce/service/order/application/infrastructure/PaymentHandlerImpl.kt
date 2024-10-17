package com.commerce.service.order.application.infrastructure

import com.commerce.service.order.application.usecase.component.PaymentHandler
import com.commerce.common.model.orders.OrdersDetailInfo
import org.springframework.stereotype.Component

@Component
class PaymentHandlerImpl: PaymentHandler {
    override fun processPayment(order: OrdersDetailInfo) {
        // 결제 처리
        // 임시로 성공 처리
    }
}