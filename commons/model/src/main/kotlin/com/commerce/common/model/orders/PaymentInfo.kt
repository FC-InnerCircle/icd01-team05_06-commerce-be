package com.commerce.common.model.orders

data class PaymentInfo(
    val method: String,
    val depositorName: String
)
