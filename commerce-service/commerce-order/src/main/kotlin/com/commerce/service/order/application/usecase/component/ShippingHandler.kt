package com.commerce.service.order.application.usecase.component

import com.commerce.service.order.application.usecase.dto.OrdersDto

/**
 * ShippingHandler
 * - 배송 준비
 */
interface ShippingHandler {
    fun arrangeShipping(order: OrdersDto)
}