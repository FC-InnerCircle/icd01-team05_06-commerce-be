package com.commerce.common.model.product

import java.math.BigDecimal
import java.time.LocalDateTime

class Product(
    val id: Long? = 0,
    val title: String,
    val author: String,
    val price: BigDecimal,
    val discountedPrice: BigDecimal,
    val publisher: String,
    val publishDate: LocalDateTime,
    val isbn: String,
    val description: String,
    val pages: Int,
    val coverImage: String,
    val previewLink: String,
    val stockQuantity: Int,
    val rating: Double,
    val status: SaleStatus,
    // val category: ProductCategoryInfoDto,
)
