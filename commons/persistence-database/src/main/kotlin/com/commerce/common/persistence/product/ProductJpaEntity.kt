package com.commerce.common.persistence.product

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.SaleStatus
import com.commerce.common.persistence.BaseTimeEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.AbstractAuditable_.createdBy
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

    @Column(nullable = false)
    val isHotNew: Boolean,
    @Column(nullable = false)
    val isRecommend: Boolean,
    @Column(nullable = false)
    val isBestseller: Boolean,
    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime,
    ) : BaseTimeEntity(){
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
        isHotNew = isHotNew,
        isRecommend = isRecommend,
        isBestseller = isBestseller
     )
    }
}

fun Product.toJpaEntity(categoryId: Long): ProductJpaEntity {
    return ProductJpaEntity(
        id = this.id,
        title = this.title,
        author = this.author,
        price = this.price,
        discountedPrice = this.discountedPrice,
        publisher = this.publisher,
        publishDate = this.publishDate,
        isbn = this.isbn,
        description = this.description,
        pages = this.pages,
        coverImage = this.coverImage,
        previewLink = this.previewLink,
        stockQuantity = this.stockQuantity,
        rating = this.rating,
        status = this.status,
        categoryId = categoryId,
        isHotNew = this.isHotNew,
        isRecommend = this.isRecommend,
        isBestseller = this.isBestseller,
        deletedAt = LocalDateTime.now(),
    )
}