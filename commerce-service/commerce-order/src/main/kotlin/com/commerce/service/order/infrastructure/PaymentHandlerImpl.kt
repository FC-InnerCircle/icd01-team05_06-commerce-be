package com.commerce.service.order.infrastructure

import com.commerce.service.order.application.usecase.component.PaymentHandler
import com.commerce.service.order.application.usecase.dto.OrdersDto
import org.springframework.stereotype.Component

@Component
class PaymentHandlerImpl: PaymentHandler {
    override fun processPayment(order: OrdersDto) {
        // 결제 처리
        // 임시로 성공 처리
    }
}