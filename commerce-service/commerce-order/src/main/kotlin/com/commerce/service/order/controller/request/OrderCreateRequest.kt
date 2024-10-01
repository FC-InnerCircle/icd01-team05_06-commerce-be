package com.commerce.service.order.controller.request

import com.commerce.common.model.member.Member
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.exception.InvalidInputException
import com.commerce.service.order.controller.common.request.CommonRequest
import java.math.BigDecimal

data class OrderCreateRequest(
    val products: List<ProductInfo>,
    val deliveryInfo: DeliveryInfo,
    val paymentInfo: PaymentInfo,
    val agreementInfo: AgreementInfo
) : CommonRequest {
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
//        val cardNumber: String,
//        val expirationDate: String,
//        val cvv: String
    )

    data class AgreementInfo(
        val termsOfService: Boolean,
        val privacyPolicy: Boolean,
        val ageVerification: Boolean
    )

    fun toCommand(member: Member) = CreateOrderCommand(
        member = member,
        products = products.map { CreateOrderCommand.ProductInfo(it.id, it.quantity) },
        deliveryInfo = CreateOrderCommand.DeliveryInfo(
            deliveryInfo.recipient,
            deliveryInfo.phoneNumber,
            deliveryInfo.email,
            deliveryInfo.streetAddress,
            deliveryInfo.detailAddress,
            deliveryInfo.postalCode
        ),
        paymentInfo = CreateOrderCommand.PaymentInfo(
            paymentInfo.method,
            paymentInfo.totalAmount,
            paymentInfo.depositorName
        ),
        agreementInfo = CreateOrderCommand.AgreementInfo(
            agreementInfo.termsOfService,
            agreementInfo.privacyPolicy,
            agreementInfo.ageVerification
        )
    )

    init {
        validate()
    }

    override fun validate() {
        products.forEach {
            if (it.id <= 0) {
                throw InvalidInputException("Invalid product id: ${it.id}")
            }
            if (it.quantity <= 0) {
                throw InvalidInputException("Invalid product quantity: ${it.quantity}")
            }
        }
        if (deliveryInfo.recipient.isBlank()) {
            throw InvalidInputException("Invalid recipient: ${deliveryInfo.recipient}")
        }
        if (deliveryInfo.phoneNumber.toLong() <= 0) {
            throw InvalidInputException("Invalid phone number: ${deliveryInfo.phoneNumber}")
        }
        if (deliveryInfo.email.isBlank()) {
            throw InvalidInputException("Invalid email: ${deliveryInfo.email}")
        }
        if (deliveryInfo.streetAddress.isBlank()) {
            throw InvalidInputException("Invalid street address: ${deliveryInfo.streetAddress}")
        }
        if (deliveryInfo.detailAddress.isBlank()) {
            throw InvalidInputException("Invalid detail address: ${deliveryInfo.detailAddress}")
        }
        if (deliveryInfo.postalCode.isBlank()) {
            throw InvalidInputException("Invalid postal code: ${deliveryInfo.postalCode}")
        }
        if (paymentInfo.method.isBlank()) {
            throw InvalidInputException("Invalid payment method: ${paymentInfo.method}")
        }
        if (paymentInfo.totalAmount.toInt() <= 0) {
            throw InvalidInputException("Invalid total amount: ${paymentInfo.totalAmount}")
        }
        if (paymentInfo.depositorName.isBlank()) {
            throw InvalidInputException("Invalid depositor name: ${paymentInfo.depositorName}")
        }
        if (!agreementInfo.termsOfService) {
            throw InvalidInputException("Terms of service agreement is required")
        }
        if (!agreementInfo.privacyPolicy) {
            throw InvalidInputException("Privacy policy agreement is required")
        }
        if (!agreementInfo.ageVerification) {
            throw InvalidInputException("Age verification agreement is required")
        }
    }
}