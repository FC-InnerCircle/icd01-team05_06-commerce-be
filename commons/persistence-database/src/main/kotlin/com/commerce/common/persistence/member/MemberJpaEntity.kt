package com.commerce.common.persistence.member

import jakarta.persistence.*

@Entity
@Table
class MemberJpaEntity(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(nullable = false)
    val name: String? = null
)