package com.commerce.service.order.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.orders.OrdersResult
import com.commerce.common.model.product.ProductRepository
import com.commerce.service.order.application.usecase.OrderUseCase
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.command.OrderListCommand
import com.commerce.service.order.application.usecase.component.PaymentHandler
import com.commerce.service.order.application.usecase.component.ProductHandler
import com.commerce.service.order.application.usecase.component.ShippingHandler
import com.commerce.service.order.application.usecase.exception.OrderNotFoundException
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.response.OrderSummary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val ordersRepository: OrdersRepository,
    private val productRepository: ProductRepository,

    private val productHandler: ProductHandler,
    private val shippingHandler: ShippingHandler,
    private val paymentHandler: PaymentHandler
) : OrderUseCase {

    /**
     * 주문 생성 로직
     * 1. 상품 관련 처리 (ProductHandler)
     * 2. 결제 처리 (PaymentHandler)
     * 3. 상품 재고 업데이트 (ProductHandler)
     * 4. 배송 처리 (ShippingHandler)
     * 5. 주문 완료 처리 (ProductHandler)
     */
    override fun order(command: CreateOrderCommand): OrderCreateResponse {

        // 1. 상품 및 재고 확인 (다수의 상품을 한번에 확인)
        val products = productHandler.getProducts(command.products)

        // 2. 상품이 존재하지 않거나 재고가 부족한 경우(다수의 상품을 한번에 확인)
        productHandler.checkAvailableProducts(products)

        // 3. 고객 정보 조회
        // 4. (고객 또는 주문자 정보 기반)으로 주문 생성 (주문번호 생성)
        // 주문번호는 ("ORD-20240815-001") 이와 같이 "ORD-날짜-랜덤숫자3자리" 형태로 생성된다.
        // [주문상태] PENDING: 주문 생성
        val order = productHandler.createOrder(command, products)

        // 5. 결제 처리 (임시 처리)
        // [주문상태] PROCESSING: 주문 처리중
        paymentHandler.processPayment(order)

        // 6. 재고 업데이트 및 배송 처리 (비동기)
        runBlocking {
            launch {
                productHandler.updateStock(order)
            }
            launch {
                // 배송 처리 (임시 처리)
                // [주문상태] COMPLETED: 주문 완료
                shippingHandler.arrangeShipping(order)
            }
        }

        // 7. 주문 완료 처리 (주문 정보 저장)
        // [주문상태] COMPLETED: 주문 완료
        return productHandler.completeOrder(order)
    }

    override fun getOrder(command: OrderListCommand) : OrderListResponse {
        val ordersPage = ordersRepository.findByMemberIdAndOrderDateBetween(
            command.member.id, command.orderDate, command.endDate, command.status, command.page, command.size, command.sortBy
        )

        val productIds = ordersPage.data.map { it.orderProducts[0].productId }.distinct()
        val products = productRepository.findByProductIdIn(productIds).associateBy { it.id }

        return OrderListResponse(
            products = ordersPage.data.map {
                OrderSummary(
                    id = it.id,
                    orderNumber = it.orderNumber,
                    content = "${products[it.orderProducts[0].productId]!!.title}${if (it.orderProducts.size > 1) " 외 ${it.orderProducts.size - 1}권" else ""}",
                    orderDate = it.orderDate.toString(),
                    status = it.status,
                    price = it.price.toDouble(),
                    discountedPrice = it.discountedPrice.toDouble(),
                    memberName = command.member.name,
                    recipient = it.deliveryInfo.recipient
                )
            },
            paginationInfo = ordersPage.pagination
        )
    }

    override fun getOrderResult(member: Member, id: Long): OrdersResult {
        return ordersRepository.findResultByIdAndMemberId(id, member.id) ?: throw OrderNotFoundException(id)
    }
}