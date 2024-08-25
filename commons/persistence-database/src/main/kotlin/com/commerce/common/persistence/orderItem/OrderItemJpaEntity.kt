package com.commerce.common.persistence.orderItem

import com.commerce.common.persistence.order.OrderJpaEntity as Order
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "order_items")
data class OrderItemJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: Order,

    @Column(name = "item_id")
    val itemId: Long,

    val quantity: Int,

    @Column(name = "price_at_purchase")
    val priceAtPurchase: BigDecimal,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
