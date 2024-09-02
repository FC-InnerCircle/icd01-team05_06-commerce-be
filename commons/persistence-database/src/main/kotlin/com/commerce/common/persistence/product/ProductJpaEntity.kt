package com.commerce.common.persistence.product

import com.commerce.common.model.product.SaleStatus
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
    val descripton: String,
    val images: List<String> =  mutableListOf(),
    val quantity: Int,
    @Column(name = "sale_status")
    val saleStatus: SaleStatus,
)
