package com.commerce.service.order.applicaton.usecase

import com.commerce.common.model.member.Member
import com.commerce.service.order.applicaton.usecase.command.PatchShoppingCartCommand
import com.commerce.service.order.applicaton.usecase.command.PostShoppingCartCommand

interface ShoppingCartUseCase {
    fun post(member: Member, command: PostShoppingCartCommand)

    fun patch(shoppingCartId: Long, command: PatchShoppingCartCommand)

    fun delete(shoppingCartId: Long)
}