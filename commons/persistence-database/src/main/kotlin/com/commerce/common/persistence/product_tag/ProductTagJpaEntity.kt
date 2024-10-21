package com.commerce.common.persistence.product_tag

import com.commerce.common.persistence.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_tag")
class ProductTagJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false)
    val name: String
) : BaseTimeEntity()