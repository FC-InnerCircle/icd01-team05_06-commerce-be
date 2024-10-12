package com.commerce.common.model.orders

import com.commerce.common.model.orderProduct.OrderProduct
import java.math.BigDecimal
import java.time.LocalDateTime

data class Orders(
    val id: Long,
    val memberId: Long,
    val orderNumber: OrderNumber,
    val ordererInfo: OrdererInfo,
    val deliveryInfo: DeliveryInfo,
    val paymentInfo: PaymentInfo,
    val discountedPrice: BigDecimal,
    val price: BigDecimal,
    val status: OrderStatus,
    val orderDate: LocalDateTime,
    val orderProducts: List<OrderProduct>
) {

    companion object {
        fun OrdersDetailInfo.createOrder(): Orders {
            return Orders(
                id = 0, // Assuming new order, set id to 0 or handle as needed
                memberId = this.memberId,
                orderNumber = this.orderNumber,
                ordererInfo = this.orderer,
                deliveryInfo = this.deliveryInfo,
                paymentInfo = this.paymentInfo,
                discountedPrice = this.products.sumOf { it.product.discountedPrice.times(BigDecimal(it.quantity)) },
                price = this.products.sumOf { it.product.price.times(BigDecimal(it.quantity)) },
                status = this.orderStatus,
                orderDate = LocalDateTime.now(),
                orderProducts = this.products.map {
                    OrderProduct(
                        id = 0, // Assuming new order product, set id to 0 or handle as needed
                        orderId = 0, // Will be set when saving the order
                        productId = it.product.id!!,
                        quantity = it.quantity.toLong(),
                        price = it.product.price.times(BigDecimal(it.quantity)),
                        discountedPrice = it.product.discountedPrice.times(BigDecimal(it.quantity))
                    )
                }.toMutableList()
            )
        }
    }

}