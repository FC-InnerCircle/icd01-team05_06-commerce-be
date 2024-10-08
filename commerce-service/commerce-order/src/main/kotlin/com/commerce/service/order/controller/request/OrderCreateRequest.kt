package com.commerce.service.order.controller.request

import com.commerce.common.model.member.Member
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.exception.InvalidInputException
import com.commerce.service.order.controller.common.request.CommonRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.math.BigDecimal

data class OrderCreateRequest(
    @field:Valid @field:NotEmpty(message = "상품 목록은 비어있을 수 없습니다")
    val products: List<ProductInfo>,
    @field:Valid
    val deliveryInfo: DeliveryInfo,
    @field:Valid
    val paymentInfo: PaymentInfo,
    @field:Valid
    val agreementInfo: AgreementInfo
) : CommonRequest {
    data class ProductInfo(
        @field:Positive(message = "상품 ID는 양수여야 합니다")
        val id: Long,
        @field:Positive(message = "수량은 양수여야 합니다")
        val quantity: Int
    )

    data class DeliveryInfo(
        @field:NotBlank(message = "수령인 이름은 비어있을 수 없습니다")
        val recipient: String,
        @field:Pattern(regexp = "\\d+", message = "전화번호는 숫자만 포함해야 합니다")
        @field:NotBlank(message = "전화번호는 비어있을 수 없습니다")
        val phoneNumber: String,
        @field:Email(message = "유효하지 않은 이메일 형식입니다")
        @field:NotBlank(message = "이메일은 비어있을 수 없습니다")
        val email: String,
        @field:NotBlank(message = "도로명 주소는 비어있을 수 없습니다")
        val streetAddress: String,
        @field:NotBlank(message = "상세 주소는 비어있을 수 없습니다")
        val detailAddress: String,
        @field:NotBlank(message = "우편번호는 비어있을 수 없습니다")
        val postalCode: String
    )

    data class PaymentInfo(
        @field:NotBlank(message = "결제 방법은 비어있을 수 없습니다")
        val method: String,
        @field:Positive(message = "총 금액은 양수여야 합니다")
        val totalAmount: BigDecimal,
        @field:NotBlank(message = "입금자명은 비어있을 수 없습니다")
        val depositorName: String
    )

    data class AgreementInfo(
        @field:AssertTrue(message = "서비스 이용 약관에 동의해야 합니다")
        val termsOfService: Boolean,
        @field:AssertTrue(message = "개인정보 처리방침에 동의해야 합니다")
        val privacyPolicy: Boolean,
        @field:AssertTrue(message = "연령 확인에 동의해야 합니다")
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
        if (products.isEmpty()) {
            throw InvalidInputException("상품 목록은 비어있을 수 없습니다")
        }
    }
}