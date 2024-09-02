package com.commerce.common.model.product

import java.math.BigDecimal

class Product(
    val id: Long = 0,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val images: List<String> = mutableListOf(),
    val quantity: Int,
    val salesStatus: SaleStatus,
) {
}
