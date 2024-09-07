package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.Orders
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.persistence.orderProduct.OrderProductJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import org.springframework.data.domain.Pageable

@Repository
class OrdersRepositoryImpl (
    private val ordersJpaRepository: OrdersJpaRepository
) : OrdersRepository {
    override fun findByCreatedAtBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: Orders.OrderStatus,
        pageable: Pageable
    ): Page<Orders> {
        val orderJpaEntities = ordersJpaRepository.findByCreatedAtBetweenAndStatus(startDate, endDate, status.toJpaStatus(), pageable)
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }

    override fun findByCreatedAtBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<Orders> {
        val orderJpaEntities = ordersJpaRepository.findByCreatedAtBetween(startDate, endDate, pageable)
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }

    private fun OrdersJpaEntity.toOrder(): Orders {
        return Orders(
            id = this.id,
            memberId = this.memberId,
            streetAddress = this.streetAddress,
            detailAddress = this.detailAddress,
            postalCode = this.postalCode,
            orderNumber = this.orderNumber,
            paymentMethod = this.paymentMethod,
            recipient  = this.recipient,
            content = this.content,
            discountedPrice = this.discountedPrice,
            price = this.price,
            status = Orders.OrderStatus.valueOf(this.status.name),
            orderDate = this.orderDate,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            orderProducts = this.orderProducts.map { it.toOrderProducts() }
        )
    }

    private fun OrderProductJpaEntity.toOrderProducts(): OrderProduct {
        return OrderProduct(
            id = this.id,
            orderId = this.order?.id ?: throw IllegalStateException("Order is not set"),
            productId = this.productId,
            quantity = this.quantity,
            price = this.price,
            discountedPrice = this.discountedPrice,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun Orders.OrderStatus.toJpaStatus(): OrdersJpaEntity.OrderStatus {
        return OrdersJpaEntity.OrderStatus.valueOf(this.name)
    }
}