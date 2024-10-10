package com.commerce.service.order.application.usecase.component

import com.commerce.common.model.orders.OrdersDetailInfo

/**
 * ShippingHandler
 * - 배송 준비
 */
interface ShippingHandler {
    fun arrangeShipping(order: OrdersDetailInfo)
}