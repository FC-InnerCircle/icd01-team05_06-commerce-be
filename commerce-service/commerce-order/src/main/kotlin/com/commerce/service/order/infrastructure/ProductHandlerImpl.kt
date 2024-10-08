package com.commerce.service.order.infrastructure

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.orderProduct.OrderProductRepository
import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.component.ProductHandler
import com.commerce.service.order.application.usecase.converter.toOrder
import com.commerce.service.order.application.usecase.dto.OrdersDto
import com.commerce.service.order.application.usecase.exception.InsufficientStockException
import com.commerce.service.order.application.usecase.exception.OrderCreationException
import com.commerce.service.order.application.usecase.exception.ProductNotFoundException
import com.commerce.service.order.application.usecase.vo.OrderNumber
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.response.OrderCreateResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ProductHandlerImpl(
    private val productRepository: ProductRepository,
    private val memberRepository: MemberRepository,
    private val shoppingCartRepository: ShoppingCartRepository,
    private val ordersRepository: OrdersRepository,
    private val orderProductRepository: OrderProductRepository
) : ProductHandler {

    // 상품 정보 조회
    override fun getProducts(productIds: List<Long>): List<Product> {
        return productIds.map { productId ->
            productRepository.findById(productId)
                ?: throw ProductNotFoundException(productId)
        }
    }

override fun createOrder(member: Member, command: CreateOrderCommand): OrdersDto {
    try {
        // 고객 정보 조회
        val customer = memberRepository.findById(member.id)
            ?: throw OrderCreationException("Member not found: ${member.id}")

        // 주문 생성 (배송정보, 결제정보 포함)
        val order = OrdersDto(
            member = customer,
            customer = command.deliveryInfo.recipient ?: customer.name,
            orderNumber = OrderNumber.createOrderNumber(),
            products = command.products.map { productInfo ->
                val product = productRepository.findById(productInfo.id)
                    ?: throw ProductNotFoundException(productInfo.id)
                OrdersDto.OrderProduct(product, productInfo.quantity)
            },
            deliveryInfo = OrdersDto.DeliveryInfo(
                recipient = command.deliveryInfo.recipient ?: customer.name,
                postalCode = command.deliveryInfo.postalCode,
                streetAddress = command.deliveryInfo.streetAddress,
                detailAddress = command.deliveryInfo.detailAddress
            ),
            paymentInfo = OrdersDto.PaymentInfo(
                method = command.paymentInfo.method,
                totalAmount = command.paymentInfo.totalAmount,
                depositorName = command.paymentInfo.depositorName
            ),
            ordersInfo = OrdersDto.OrdersInfo(
                content = "", // 주문 내용 (임시 처리)
                OrderStatus = OrderStatus.PENDING
            ))

        return order
    } catch (e: Exception) {
        throw OrderCreationException("Failed to create order: ${e.message}")
    }
}

    override fun updateStock(order: OrdersDto) {
        // 주문한 상품들의 재고를 감소시킨다.
        order.products.forEach { orderProduct ->
            val product = orderProduct.product.id?.let { productRepository.findById(it) }
                ?: throw orderProduct.product.id?.let { ProductNotFoundException(it) }!!

            product.stockQuantity -= orderProduct.quantity
            productRepository.save(product)
        }
    }

    // 저장된 장바구니 정보 삭제 (보류 - 비즈니스 정책 고려)
    // 주문 완료 처리
    override fun completeOrder(order: OrdersDto): OrderCreateResponse {
        // order의 주문상태 완료로 변경
        order.changeOrderStatus(OrderStatus.COMPLETED)

        // 주문 정보 저장
        // Orders, OrderProduct 저장
        ordersRepository.save(order.toOrder())

        // 주문 완료 응답 생성
        return OrderCreateResponse(
            orderNumber = order.orderNumber.value,
            orderStatus = order.ordersInfo.OrderStatus.name,
            orderDate = LocalDateTime.now().toString(),
            totalAmount = order.paymentInfo.totalAmount,
            products = order.products.map { orderProduct ->
                OrderCreateResponse.ProductSummary(
                    id = orderProduct.product.id ?: -1,
                    name = orderProduct.product.title,
                    quantity = orderProduct.quantity,
                    price = orderProduct.product.price
                )
            }
        )

    }

    // 재고 확인(다수의 상품을 한번에 확인)
    override fun checkAvailableProducts(products: List<Product>, command: List<CreateOrderCommand.ProductInfo>) {
        // products에 있는 개별 상품들의 재고가 request에 있는 상품들의 수량보다 많은지 확인
        val insufficientProducts = products.zip(command)
            .filter { (product, productInfo) -> product.stockQuantity < productInfo.quantity }
            .map { (product, _) -> product.id }

        if (insufficientProducts.isNotEmpty()) {
            throw InsufficientStockException(insufficientProducts)
        }
    }
}