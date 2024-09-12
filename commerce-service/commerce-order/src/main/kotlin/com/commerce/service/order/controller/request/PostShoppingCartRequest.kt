package com.commerce.service.order.controller.request

import com.commerce.service.order.applicaton.usecase.command.PostShoppingCartCommand
import jakarta.validation.constraints.Min

data class PostShoppingCartRequest(
    val productId: Long,

    @Min(1)
    val quantity: Int
) {
    fun toCommand() = PostShoppingCartCommand(
        productId = productId,
        quantity = quantity
    )
}
