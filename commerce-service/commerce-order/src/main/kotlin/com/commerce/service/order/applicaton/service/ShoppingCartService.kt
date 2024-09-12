package com.commerce.service.order.applicaton.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.shopping_cart.ShoppingCart
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import com.commerce.service.order.applicaton.usecase.ShoppingCartUseCase
import com.commerce.service.order.applicaton.usecase.command.PostShoppingCartCommand
import org.springframework.stereotype.Service

@Service
class ShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository,
) : ShoppingCartUseCase {

    override fun post(member: Member, command: PostShoppingCartCommand) {
        // TODO : 상품이 존재하는지 확인
        val shoppingCart = shoppingCartRepository.findByMemberIdAndProductId(member.id, command.productId)?.let {
            it.updateQuantity(it.quantity + command.quantity)
        } ?: ShoppingCart(
            memberId = member.id,
            productId = command.productId,
            quantity = command.quantity
        )
        shoppingCartRepository.save(shoppingCart)
    }
}