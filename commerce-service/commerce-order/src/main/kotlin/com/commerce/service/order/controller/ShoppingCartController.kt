package com.commerce.service.order.controller

import com.commerce.common.model.member.Member
import com.commerce.common.response.CommonResponse
import com.commerce.service.order.applicaton.usecase.ShoppingCartUseCase
import com.commerce.service.order.applicaton.usecase.dto.ShoppingCartListDto
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.request.PatchShoppingCartRequest
import com.commerce.service.order.controller.request.PostShoppingCartRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

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

    @PatchMapping("/{shoppingCartId}")
    fun patch(
        @AuthenticationPrincipal member: Member,
        @PathVariable shoppingCartId: Long,
        @RequestBody request: PatchShoppingCartRequest
    ): CommonResponse<Unit> {
        shoppingCartUseCase.patch(shoppingCartId, request.toCommand())
        return CommonResponse.ok()
    }

    @DeleteMapping("/{shoppingCartId}")
    fun delete(
        @AuthenticationPrincipal member: Member,
        @PathVariable shoppingCartId: Long
    ): CommonResponse<Unit> {
        shoppingCartUseCase.delete(shoppingCartId)
        return CommonResponse.ok()
    }

    @GetMapping
    fun getList(
        @AuthenticationPrincipal member: Member
    ): CommonResponse<ShoppingCartListDto> {
        return CommonResponse.ok(shoppingCartUseCase.getList(member))
    }
}