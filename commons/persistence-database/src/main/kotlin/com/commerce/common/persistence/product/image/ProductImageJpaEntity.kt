package com.commerce.common.persistence.product.image

import com.commerce.common.persistence.product.ProductJpaEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_image")
class ProductImageJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductJpaEntity,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,
)