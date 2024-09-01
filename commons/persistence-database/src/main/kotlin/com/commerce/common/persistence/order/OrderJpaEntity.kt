package com.commerce.common.persistence.order

import com.commerce.common.persistence.orderItem.OrderItemJpaEntity as OrderItem
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class OrderJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id")
    val memberId: Long,

    @Column(name = "street_address")
    val streetAddress: String,

    @Column(name = "detail_address")
    val detailAddress: String,

    @Column(name = "postal_code")
    val postalCode: String,

    @Enumerated(EnumType.STRING)
    val status: OrderStatus,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderItems: MutableList<OrderItem> = mutableListOf()

    enum class OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
}