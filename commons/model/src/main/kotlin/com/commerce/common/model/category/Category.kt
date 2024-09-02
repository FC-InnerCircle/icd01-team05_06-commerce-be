package com.commerce.common.model.category

import java.time.LocalDateTime

class Category(
    val id: Long? = 0,
    val parentId: Long? = 0,
    val name: String,
    val depth: Int,
    // val createdAt: LocalDateTime,
    // val updatedAt: LocalDateTime,
)