package com.commerce.service.order.application.usecase.command

import com.commerce.common.model.member.Member
import com.commerce.service.order.controller.request.OrderCreateRequest
import java.math.BigDecimal

data class CreateOrderCommand(
    val member: Member,
    val products: List<ProductInfo>,
    val deliveryInfo: DeliveryInfo,
    val paymentInfo: PaymentInfo,
    val agreementInfo: AgreementInfo
) {
    data class ProductInfo(
        val id: Long,
        val quantity: Int
    )

    data class DeliveryInfo(
        val recipient: String,
        val phoneNumber: String,
        val email: String,
        val streetAddress: String,
        val detailAddress: String,
        val postalCode: String
    )

    data class PaymentInfo(
        val method: String,
        val totalAmount: BigDecimal,
        val depositorName: String
    )

    data class AgreementInfo(
        val termsOfService: Boolean,
        val privacyPolicy: Boolean,
        val ageVerification: Boolean
    )
}
