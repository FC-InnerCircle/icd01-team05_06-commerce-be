package com.commerce.common.persistence.product

import com.commerce.common.persistence.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_tag")
class ProductTagJpaEntity(
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var productId: Long
) : BaseTimeEntity()