package com.commerce.service.order.infrastructure

import com.commerce.common.model.orders.*
import com.commerce.common.model.orders.Orders.Companion.createOrder
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.product.ProductWithQuantity
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.component.ProductHandler
import com.commerce.service.order.application.usecase.exception.InsufficientStockException
import com.commerce.service.order.controller.response.OrderCreateResponse
import org.springframework.stereotype.Component

@Component
class ProductHandlerImpl(
    private val productRepository: ProductRepository,
    private val ordersRepository: OrdersRepository,
    private val orderNumberRepository: OrderNumberRepository,
    private val shoppingCartRepository: ShoppingCartRepository
) : ProductHandler {

    // 상품 정보 조회
    override fun getProducts(productInfos: List<CreateOrderCommand.ProductInfo>): List<ProductWithQuantity> {
        val products = productRepository.findByProductIdIn(productInfos.map { it.id })
        return products.map { product ->
            ProductWithQuantity(
                product,
                productInfos.find { it.id == product.id }!!.quantity
            )
        }
    }

    override fun createOrder(command: CreateOrderCommand, productInfos: List<ProductWithQuantity>): OrdersDetailInfo {
        // 주문 생성 (배송정보, 결제정보 포함)
        return OrdersDetailInfo(
            memberId = command.member.id,
            orderer = OrdererInfo(
                name = command.ordererInfo.name,
                phoneNumber = command.ordererInfo.phoneNumber,
                email = command.ordererInfo.email
            ),
            orderNumber = OrderNumber.createOrderNumber(orderNumberRepository),
            products = productInfos,
            deliveryInfo = DeliveryInfo(
                recipient = command.deliveryInfo.recipient,
                phoneNumber = command.deliveryInfo.phoneNumber,
                address = command.deliveryInfo.address,
                memo = command.deliveryInfo.memo,
            ),
            paymentInfo = PaymentInfo(
                method = command.paymentInfo.method,
                depositorName = command.paymentInfo.depositorName
            ),
            orderStatus = OrderStatus.COMPLETED
        )
    }

    override fun updateStock(orderInfo: OrdersDetailInfo) {
        // 주문한 상품들의 재고를 감소시킨다.
        orderInfo.products.forEach { orderProduct ->
            orderProduct.product.stockQuantity -= orderProduct.quantity
            productRepository.save(orderProduct.product)
        }
    }

    // 주문 완료 처리
    override fun completeOrder(orderInfo: OrdersDetailInfo): OrderCreateResponse {

        // 저장된 장바구니 정보 삭제
        val productIds = orderInfo.products.map { orderProduct -> orderProduct.product.id!! }
        shoppingCartRepository.deleteByMemberIdAndProductIdIn(orderInfo.memberId, productIds)

        // 주문 정보 저장
        // Orders, OrderProduct 저장
        val orders = ordersRepository.save(orderInfo.createOrder())

        // 주문 완료 응답 생성
        return OrderCreateResponse(
            id = orders.id,
            orderNumber = orderInfo.orderNumber.value,
            orderStatus = orderInfo.orderStatus.name,
            orderDate = orders.orderDate.toString(),
            products = orderInfo.products.map { orderProduct ->
                OrderCreateResponse.ProductSummary(
                    id = orderProduct.product.id ?: -1,
                    name = orderProduct.product.title,
                    quantity = orderProduct.quantity,
                    price = orderProduct.product.price,
                    discountedPrice = orderProduct.product.discountedPrice,
                )
            }
        )

    }

    // 재고 확인(다수의 상품을 한번에 확인)
    override fun checkAvailableProducts(productInfos: List<ProductWithQuantity>) {
        // products에 있는 개별 상품들의 재고가 request에 있는 상품들의 수량보다 많은지 확인
        val insufficientProducts = productInfos
            .filter { it.product.stockQuantity < it.quantity }
            .map { it.product.id }

        if (insufficientProducts.isNotEmpty()) {
            throw InsufficientStockException(insufficientProducts)
        }
    }
}