package com.commerce.service.order.infrastructure

import com.commerce.service.order.application.usecase.component.ShippingHandler
import com.commerce.common.model.orders.OrdersDetailInfo
import org.springframework.stereotype.Component

@Component
class ShippingHandlerImpl: ShippingHandler {
    override fun arrangeShipping(order: OrdersDetailInfo) {
        // 배송 준비
        // 임시로 성공 처리
    }
}