package com.commerce.common.persistence.category

import com.commerce.common.model.category.Category
import jakarta.persistence.*

@Entity
@Table(name = "product_category")
class CategoryJpaEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    val parent: CategoryJpaEntity? = null,

    @OneToMany(mappedBy = "parent")
    val subcategories: List<CategoryJpaEntity> = mutableListOf(),

    val name: String,
    val depth: Int,
) {
    fun toModel(): Category = Category(
        id = id,
        parentId = parent?.id,
        name = name,
        depth = depth,
    )
}