package com.commerce.service.order.controller

import com.commerce.common.model.member.Member
import com.commerce.common.response.CommonResponse
import com.commerce.service.order.applicaton.usecase.ShoppingCartUseCase
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.request.PostShoppingCartRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiPaths.SHOPPING_CARTS)
class ShoppingCartController(
    private val shoppingCartUseCase: ShoppingCartUseCase
) {

    @PostMapping
    fun post(
        @AuthenticationPrincipal member: Member,
        @RequestBody request: PostShoppingCartRequest
    ): CommonResponse<Unit> {
        shoppingCartUseCase.post(member, request.toCommand())
        return CommonResponse.ok()
    }
}