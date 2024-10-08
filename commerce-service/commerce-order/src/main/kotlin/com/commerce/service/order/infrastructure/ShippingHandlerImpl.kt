package com.commerce.service.order.infrastructure

import com.commerce.service.order.application.usecase.component.ShippingHandler
import com.commerce.service.order.application.usecase.dto.OrdersDto
import org.springframework.stereotype.Component

@Component
class ShippingHandlerImpl: ShippingHandler {
    override fun arrangeShipping(order: OrdersDto) {
        // 배송 준비
        // 임시로 성공 처리
    }
}