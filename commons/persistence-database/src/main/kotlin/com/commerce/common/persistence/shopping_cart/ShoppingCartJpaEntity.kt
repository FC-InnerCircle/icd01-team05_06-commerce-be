package com.commerce.common.persistence.shopping_cart

import com.commerce.common.model.shopping_cart.ShoppingCart
import com.commerce.common.persistence.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "shopping_cart")
class ShoppingCartJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var memberId: Long,

    @Column(nullable = false)
    var productId: Long,

    @Column(nullable = false)
    var quantity: Int,

    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
) : BaseTimeEntity(
    createdAt = createdAt,
    updatedAt = updatedAt
) {
    fun toModel() = ShoppingCart(
        id = id,
        memberId = memberId,
        productId = productId,
        quantity = quantity,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        fun from(shoppingCart: ShoppingCart) = ShoppingCartJpaEntity(
            id = shoppingCart.id,
            memberId = shoppingCart.memberId,
            productId = shoppingCart.productId,
            quantity = shoppingCart.quantity,
            createdAt = shoppingCart.createdAt,
            updatedAt = shoppingCart.updatedAt,
        )
    }
}