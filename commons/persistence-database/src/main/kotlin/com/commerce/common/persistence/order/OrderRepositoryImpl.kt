package com.commerce.common.persistence.order

import com.commerce.common.model.order.Order
import com.commerce.common.model.order.OrderRepository
import com.commerce.common.model.orderItem.OrderItem
import com.commerce.common.persistence.orderItem.OrderItemJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
class OrderRepositoryImpl (
    private val orderJpaRepository: OrderJpaRepository
) : OrderRepository {
    override fun findByCreatedAtBetweenAndStatus(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: Order.OrderStatus,
        pageable: Pageable
    ): Page<Order> {
        val orderJpaEntities = orderJpaRepository.findByCreatedAtBetweenAndStatus(startDate, endDate, status.toJpaStatus(), pageable)
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }

    override fun findByCreatedAtBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<Order> {
        val orderJpaEntities = orderJpaRepository.findByCreatedAtBetween(startDate, endDate, pageable)
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }

    private fun OrderJpaEntity.toOrder(): Order {
        return Order(
            id = this.id,
            memberId = this.memberId,
            status = Order.OrderStatus.valueOf(this.status.name),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            streetAddress = this.streetAddress,
            detailAddress = this.detailAddress,
            postalCode = this.postalCode,
            orderItems = this.orderItems.map { it.toOrderItem() }
        )
    }

    private fun OrderItemJpaEntity.toOrderItem(): OrderItem {
        return OrderItem(
            id = this.id,
            orderId = this.order.id,
            itemId = this.itemId,
            quantity = this.quantity,
            priceAtPurchase = this.priceAtPurchase.toDouble(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun Order.OrderStatus.toJpaStatus(): OrderJpaEntity.OrderStatus {
        return OrderJpaEntity.OrderStatus.valueOf(this.name)
    }
}