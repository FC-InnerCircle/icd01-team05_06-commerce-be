package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.OrdererInfo
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class OrdererInfoEmbeddable(

    @Column(name = "orderer_name", nullable = false)
    val name: String,

    @Column(name = "orderer_phone_number", nullable = false)
    val phoneNumber: String,

    @Column(name = "orderer_email", nullable = false)
    val email: String
) {
    fun toModel() = OrdererInfo(
        name = name,
        phoneNumber = phoneNumber,
        email = email
    )

    companion object {
        fun from(ordererInfo: OrdererInfo) = with(ordererInfo) {
            OrdererInfoEmbeddable(
                name = name,
                phoneNumber = phoneNumber,
                email = email
            )
        }
    }
}
