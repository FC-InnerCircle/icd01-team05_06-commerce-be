package com.commerce.common.model.orderProduct

import com.commerce.common.model.product.Product

data class OrderProductWithInfo(
    val orderProduct: OrderProduct,
    val product: Product
)
