package com.commerce.common.persistence.orders

import com.commerce.common.model.orderProduct.OrderProduct
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
data class OrdersJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id")
    val memberId: Long,

    //TODO: Address 묶어서?? 3가지
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
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderProducts: MutableList<OrderProductJpaEntity> = mutableListOf()
}

fun OrdersJpaEntity.toOrder(): Orders {
    return Orders(
        id = this.id,
        memberId = this.memberId,
        streetAddress = this.streetAddress,
        detailAddress = this.detailAddress,
        postalCode = this.postalCode,
        orderNumber = OrderNumber(this.orderNumber),
        paymentMethod = this.paymentMethod,
        recipient  = this.recipient,
        content = this.content,
        discountedPrice = this.discountedPrice,
        price = this.price,
        status = OrderStatus.valueOf(this.status.name),
        orderDate = this.orderDate,
        orderProducts = this.orderProducts.map { it.toOrderProducts() }
    )
}

// Orders 클��스의 toEntity 확장 함수
// TODO: 함수명 from() 으로 변경
fun Orders.toJpaEntity(): OrdersJpaEntity {
    return OrdersJpaEntity(
        id = this.id,
        memberId = this.memberId,
        streetAddress = this.streetAddress,
        detailAddress = this.detailAddress,
        postalCode = this.postalCode,
        orderNumber = this.orderNumber.value,
        paymentMethod = this.paymentMethod,
        recipient = this.recipient,
        content = this.content,
        discountedPrice = this.discountedPrice,
        price = this.price,
        status = this.status,
        orderDate = this.orderDate,
    ).apply {
        orderProducts.addAll(this@toJpaEntity.orderProducts.map { it.toJpaEntity(this) })
    }
}