package com.commerce.service.order.controller.request

import com.commerce.service.order.application.usecase.command.PatchShoppingCartCommand
import jakarta.validation.constraints.Min

data class PatchShoppingCartRequest(
    @Min(1)
    val quantity: Int
) {
    fun toCommand() = PatchShoppingCartCommand(
        quantity = quantity
    )
}
