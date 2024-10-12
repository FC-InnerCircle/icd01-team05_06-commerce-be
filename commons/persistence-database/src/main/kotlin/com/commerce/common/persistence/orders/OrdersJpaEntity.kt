package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.OrderNumber
import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.Orders
import com.commerce.common.persistence.BaseTimeEntity
import com.commerce.common.persistence.orderProduct.OrderProductJpaEntity
import com.commerce.common.persistence.orderProduct.toJpaEntity
import com.commerce.common.persistence.orderProduct.toOrderProducts
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrdersJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id")
    val memberId: Long,

    @Column(name = "order_number")
    val orderNumber: String,

    @Embedded
    val ordererInfo: OrdererInfoEmbeddable,

    @Embedded
    val deliveryInfo: DeliveryInfoEmbeddable,

    @Embedded
    val paymentInfo: PaymentInfoEmbeddable,

    @Column(name = "discounted_price")
    val discountedPrice: BigDecimal,

    @Column(name = "price")
    val price: BigDecimal,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: OrderStatus,

    @Column(name = "order_date")
    val orderDate: LocalDateTime,
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderProducts: MutableList<OrderProductJpaEntity> = mutableListOf()
}

fun OrdersJpaEntity.toOrder(): Orders {
    return Orders(
        id = this.id,
        memberId = this.memberId,
        orderNumber = OrderNumber(this.orderNumber),
        ordererInfo = this.ordererInfo.toModel(),
        deliveryInfo = this.deliveryInfo.toModel(),
        paymentInfo = this.paymentInfo.toModel(),
        discountedPrice = this.discountedPrice,
        price = this.price,
        status = this.status,
        orderDate = this.orderDate,
        orderProducts = this.orderProducts.map { it.toOrderProducts() }
    )
}

// Orders 클래스의 toEntity 확장 함수
// TODO: 함수명 from() 으로 변경
fun Orders.toJpaEntity(): OrdersJpaEntity {
    return OrdersJpaEntity(
        id = this.id,
        memberId = this.memberId,
        orderNumber = this.orderNumber.value,
        ordererInfo = OrdererInfoEmbeddable.from(this.ordererInfo),
        deliveryInfo = DeliveryInfoEmbeddable.from(this.deliveryInfo),
        paymentInfo = PaymentInfoEmbeddable.from(this.paymentInfo),
        discountedPrice = this.discountedPrice,
        price = this.price,
        status = this.status,
        orderDate = this.orderDate,
    ).apply {
        orderProducts.addAll(this@toJpaEntity.orderProducts.map { it.toJpaEntity(this) })
    }
}