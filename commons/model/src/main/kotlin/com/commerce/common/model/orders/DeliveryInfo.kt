package com.commerce.common.model.orders

import com.commerce.common.model.address.Address

data class DeliveryInfo(
    val recipient: String,
    val phoneNumber: String,
    val address: Address,
    val memo: String?
)
