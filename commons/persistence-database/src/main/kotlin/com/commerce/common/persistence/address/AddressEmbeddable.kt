package com.commerce.common.persistence.address

import com.commerce.common.model.address.Address
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class AddressEmbeddable(
    @Column(nullable = false)
    val postalCode: String,

    @Column(nullable = false)
    val streetAddress: String,

    @Column(nullable = false)
    val detailAddress: String
) {
    fun toModel() = Address(
        postalCode = postalCode,
        streetAddress = streetAddress,
        detailAddress = detailAddress
    )

    companion object {
        fun from(address: Address) = AddressEmbeddable(
            postalCode = address.postalCode,
            streetAddress = address.streetAddress,
            detailAddress = address.detailAddress
        )
    }
}