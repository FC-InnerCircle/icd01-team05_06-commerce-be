package com.commerce.common.model.product

import com.commerce.common.model.category.CategoryDetail
import java.math.BigDecimal
import java.time.LocalDate

class Product(
    val id: Long? = 0,
    val title: String,
    val author: String,
    val price: BigDecimal,
    val discountedPrice: BigDecimal,
    val publisher: String,
    val publishDate: LocalDate,
    val isbn: String,
    val description: String,
    val pages: Int,
    val coverImage: String,
    val previewLink: String,
    var stockQuantity: Int,
    val rating: Double,
    val status: SaleStatus,
    val category: CategoryDetail? = null,
    val isHotNew: Boolean,
    val isRecommend: Boolean,
    val isBestseller: Boolean
)