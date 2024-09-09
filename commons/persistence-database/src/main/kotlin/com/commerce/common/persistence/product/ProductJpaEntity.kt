package com.commerce.common.persistence.product

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.SaleStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "product")
class ProductJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val author: String,
    @Column(name = "cover_image")
    val coverImage: String,
    val description: String,

    @Column(name = "discounted_price")
    val discountedPrice: BigDecimal,
    val isbn: String,
    val pages: Int,
    val previewLink: String,
    val price: BigDecimal,
    val title: String,
    val categoryId: Long? = null,
    val publisher: String,
    val rating: Double,

    @Enumerated(EnumType.STRING)
    val status: SaleStatus,

    @Column(name = "stock_quantity")
    val stockQuantity: Int,

    @Column(name = "publish_date")
    val publishDate: LocalDateTime,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,
    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime,

    ) {
    fun toModel(category: CategoryDetail?): Product {
     return Product(
         id = id,
         title = title,
         author = author,
         price = price,
         discountedPrice = discountedPrice,
         publisher = publisher,
         publishDate = publishDate,
         isbn = isbn,
         description = description,
         pages = pages,
         coverImage = coverImage,
         previewLink = previewLink,
         stockQuantity = stockQuantity,
         rating = rating,
         status = status,
         category = category,
     )
    }
}
