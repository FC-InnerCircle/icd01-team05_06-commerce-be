package com.commerce.service.product.application.usecase.dto

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.ProductWithTag
import com.commerce.common.model.product.SaleStatus
import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class ProductInfoDto(
    val id : Long?,
    val title: String,
    val author: String,
    val price: BigDecimal,
    val discountedPrice: BigDecimal,
    val publisher: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val publishDate: LocalDate,
    val isbn: String,
    val description: String,
    val pages: Int,
    val coverImage: String,
    val previewLink: String,
    val stockQuantity: Int,
    val rating: Double,
    val status: SaleStatus,
    val tags: List<String>,
    val category: CategoryDetail?,
) {
    companion object {
        fun from(model: ProductWithTag): ProductInfoDto {
            return ProductInfoDto(
                id = model.product.id,
                title = model.product.title,
                author = model.product.author,
                price = model.product.price.setScale(0, RoundingMode.DOWN),
                discountedPrice = model.product.discountedPrice.setScale(0, RoundingMode.DOWN),
                publisher = model.product.publisher,
                publishDate = model.product.publishDate,
                isbn = model.product.isbn,
                description = model.product.description,
                pages = model.product.pages,
                coverImage = model.product.coverImage,
                previewLink = model.product.previewLink,
                stockQuantity = model.product.stockQuantity,
                rating = model.product.rating,
                status = model.product.status,
                tags = model.tags,
                category = model.product.category,
            )
        }
    }
}