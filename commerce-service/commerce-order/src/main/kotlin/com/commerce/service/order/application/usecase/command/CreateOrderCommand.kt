package com.commerce.service.order.application.usecase.command

import com.commerce.common.model.address.Address
import com.commerce.common.model.member.Member

data class CreateOrderCommand(
    val member: Member,
    val products: List<ProductInfo>,
    val ordererInfo: OrdererInfo,
    val deliveryInfo: DeliveryInfo,
    val paymentInfo: PaymentInfo,
    val agreementInfo: AgreementInfo
) {
    data class ProductInfo(
        val id: Long,
        val quantity: Int
    )

    data class OrdererInfo(
        val name: String,
        val phoneNumber: String,
        val email: String
    )

    data class DeliveryInfo(
        val recipient: String,
        val phoneNumber: String,
        val address: Address,
        val memo: String?
    )

    data class PaymentInfo(
        val method: String,
        val depositorName: String
    )

    data class AgreementInfo(
        val termsOfService: Boolean,
        val privacyPolicy: Boolean,
        val ageVerification: Boolean
    )
}
