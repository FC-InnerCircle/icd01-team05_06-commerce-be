package com.commerce.service.order.controller.response

import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.OrdersResult
import java.math.BigDecimal

data class OrderResultResponse(
    val id: Long,
    val orderNumber: String,
    val orderer: OrdererInfo, // 주문자
    val products: List<ProductInfo>,
    val deliveryInfo: DeliveryInfo, // 배송정보
    val paymentInfo: PaymentInfo, // 결제정보
    val orderStatus: OrderStatus // 주문 상태
) {
    data class OrdererInfo(
        val name: String,
        val phoneNumber: String,
        val email: String
    )

    data class ProductInfo(
        val id: Long,
        val title: String,
        val author: String,
        val publisher: String,
        val coverImage: String,
        val quantity: Int,
        val price: BigDecimal,
        val discountedPrice: BigDecimal,
    )

    data class DeliveryInfo(
        val recipient: String,
        val phoneNumber: String,
        val postalCode: String,
        val streetAddress: String,
        val detailAddress: String,
        val memo: String?
    )

    data class PaymentInfo(
        val method: String,
        val depositorName: String
    )

    companion object {
        fun from(ordersResult: OrdersResult): OrderResultResponse {
            return with(ordersResult) {
                OrderResultResponse(
                    id = id,
                    orderNumber = orderNumber.value,
                    orderer = OrdererInfo(
                        name = orderer.name,
                        phoneNumber = orderer.phoneNumber,
                        email = orderer.email
                    ),
                    products = products.map {
                        ProductInfo(
                            id = it.product.id!!,
                            title = it.product.title,
                            author = it.product.author,
                            publisher = it.product.publisher,
                            coverImage = it.product.coverImage,
                            quantity = it.orderProduct.quantity.toInt(),
                            price = it.orderProduct.price,
                            discountedPrice = it.orderProduct.discountedPrice
                        )
                    }.toList(),
                    deliveryInfo = DeliveryInfo(
                        recipient = deliveryInfo.recipient,
                        phoneNumber = deliveryInfo.phoneNumber,
                        postalCode = deliveryInfo.address.postalCode,
                        streetAddress = deliveryInfo.address.streetAddress,
                        detailAddress = deliveryInfo.address.detailAddress,
                        memo = deliveryInfo.memo
                    ),
                    paymentInfo = PaymentInfo(
                        method = paymentInfo.method,
                        depositorName = paymentInfo.depositorName
                    ),
                    orderStatus = orderStatus
                )
            }
        }
    }
}
