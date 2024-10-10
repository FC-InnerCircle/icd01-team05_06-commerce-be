package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.DeliveryInfo
import com.commerce.common.persistence.address.AddressEmbeddable
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class DeliveryInfoEmbeddable(

    @Column(nullable = false)
    val recipient: String,

    @Column(name = "delivery_phone_number", nullable = false)
    val phoneNumber: String,

    @Embedded
    val address: AddressEmbeddable,

    @Column(name = "delivery_memo", nullable = false)
    val memo: String?
) {
    fun toModel() = DeliveryInfo(
        recipient = recipient,
        phoneNumber = phoneNumber,
        address = address.toModel(),
        memo = memo
    )

    companion object {
        fun from(deliveryInfo: DeliveryInfo) = with(deliveryInfo) {
            DeliveryInfoEmbeddable(
                recipient = recipient,
                phoneNumber = phoneNumber,
                address = AddressEmbeddable.from(address),
                memo = memo
            )
        }
    }
}