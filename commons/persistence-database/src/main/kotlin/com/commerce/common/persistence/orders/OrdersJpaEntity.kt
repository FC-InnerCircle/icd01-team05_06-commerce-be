package com.commerce.common.persistence.orders

import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.persistence.orderProduct.OrderProductJpaEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class OrdersJpaEntity(
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

    @Column(name = "order_number")
    val orderNumber: String,

    @Column(name = "payment_method")
    val paymentMethod: String,

    @Column(name = "recipient")
    val recipient: String,

    @Column(name = "content")
    val content: String,

    @Column(name = "discounted_price")
    val discountedPrice: BigDecimal,

    @Column(name = "price")
    val price: BigDecimal,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
//    @Enumerated(EnumType.ORDINAL)
    val status: OrderStatus,

    @Column(name = "order_date")
    val orderDate: LocalDateTime,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderProducts: MutableList<OrderProductJpaEntity> = mutableListOf()

    // 주문상태
    // PENDING: 주문 생성
    // PROCESSING: 주문 처리중
    // SHIPPED: 배송중
    // DELIVERED: 배송완료
    // CANCEL: 주문 취소
    // REFUND: 환불
    // EXCHANGE: 교환
    enum class OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCEL, REFUND, EXCHANGE
    }

    fun addOrderProduct(orderProductJpaEntity: OrderProductJpaEntity) {
        orderProducts.add(orderProductJpaEntity)
        orderProductJpaEntity.order = this // OrderProductJpaEntity의 order에 OrdersJpaEntity를 설정
    }

    fun removeOrderProduct(orderProductJpaEntity: OrderProductJpaEntity) {
        orderProducts.remove(orderProductJpaEntity)
        orderProductJpaEntity.order = null // OrderProductJpaEntity의 order를 null로 설정
    }
}