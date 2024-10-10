package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.PaymentInfo
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class PaymentInfoEmbeddable(

    @Column(name = "payment_method", nullable = false)
    val method: String,

    @Column(name = "payment_depositor", nullable = false)
    val depositorName: String
) {
    fun toModel() = PaymentInfo(
        method = method,
        depositorName = depositorName
    )

    companion object {
        fun from(paymentInfo: PaymentInfo) = with(paymentInfo) {
            PaymentInfoEmbeddable(
                method = method,
                depositorName = depositorName
            )
        }
    }
}
