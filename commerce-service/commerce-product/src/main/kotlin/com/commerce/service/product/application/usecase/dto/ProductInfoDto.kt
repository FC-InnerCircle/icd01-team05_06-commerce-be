package com.commerce.service.product.application.usecase.dto

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.Product
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
    val category: CategoryDetail?,
) {
    companion object {
        fun from(product: Product): ProductInfoDto {
            return ProductInfoDto(
                id = product.id,
                title = product.title,
                author = product.author,
                price = product.price.setScale(0, RoundingMode.DOWN),
                discountedPrice = product.discountedPrice.setScale(0, RoundingMode.DOWN),
                publisher = product.publisher,
                publishDate = product.publishDate,
                isbn = product.isbn,
                description = product.description,
                pages = product.pages,
                coverImage = product.coverImage,
                previewLink = product.previewLink,
                stockQuantity = product.stockQuantity,
                rating = product.rating,
                status = product.status,
                category = product.category,
            )
        }
    }
}