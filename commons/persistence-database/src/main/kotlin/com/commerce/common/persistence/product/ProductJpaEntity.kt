package com.commerce.common.persistence.product

import com.commerce.common.model.product.Product
import com.commerce.common.model.product.SaleStatus
import com.commerce.common.persistence.category.CategoryJpaEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "product")
class ProductJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,
    val price: BigDecimal,
    val description: String,
    val images: List<String> =  mutableListOf(),
    val quantity: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val categoryJpaEntity: CategoryJpaEntity,

    @Column(name = "sale_status")
    val saleStatus: SaleStatus,
) {
    fun toModel(): Product {
     return Product(
         id = id,
         name = name,
         price = price,
         description = description,
         images = images,
         quantity = quantity,
         saleStatus = saleStatus,
     )
    }
}
