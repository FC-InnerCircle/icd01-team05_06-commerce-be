package com.commerce.service.order.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.shopping_cart.ShoppingCart
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import com.commerce.service.order.application.usecase.ShoppingCartUseCase
import com.commerce.service.order.application.usecase.command.PatchShoppingCartCommand
import com.commerce.service.order.application.usecase.command.PostShoppingCartCommand
import com.commerce.service.order.application.usecase.dto.ShoppingCartListDto
import org.springframework.http.HttpStatus
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

    override fun patch(shoppingCartId: Long, command: PatchShoppingCartCommand) {
        val shoppingCart = shoppingCartRepository.findById(shoppingCartId)
            ?: throw CustomException(
                HttpStatus.NOT_FOUND,
                ErrorCode.SHOPPING_CART_NOT_FOUND
            )

        shoppingCartRepository.save(shoppingCart.updateQuantity(command.quantity))
    }

    override fun delete(shoppingCartId: Long) {
        shoppingCartRepository.deleteById(shoppingCartId)
    }

    override fun getList(member: Member): ShoppingCartListDto {
        return ShoppingCartListDto(shoppingCartRepository.findProducts(member.id))
    }
}