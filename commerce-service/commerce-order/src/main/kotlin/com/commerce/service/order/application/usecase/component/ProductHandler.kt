package com.commerce.service.order.application.usecase.component

import com.commerce.common.model.member.Member
import com.commerce.common.model.product.Product
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.dto.OrdersDto
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.response.OrderCreateResponse

/**
 * ProductHandler
 * - 상품 정보 조회(다수의 상품을 한번에 확인)
 * - 주문 생성 (고객 또는 주문자 정보 기반)
 * - 재고 업데이트
 * - 주문 완료 처리
 * - 상품이 존재하지 않거나 재고가 부족한 경우(다수의 상품을 한번에 확인)
 */
interface ProductHandler {
    fun getProducts(productIds: List<Long>): List<Product>
    fun createOrder(member: Member, command: CreateOrderCommand): OrdersDto
    fun updateStock(order: OrdersDto)
    fun completeOrder(order: OrdersDto): OrderCreateResponse
    fun checkAvailableProducts(products: List<Product>, command: List<CreateOrderCommand.ProductInfo>)
}