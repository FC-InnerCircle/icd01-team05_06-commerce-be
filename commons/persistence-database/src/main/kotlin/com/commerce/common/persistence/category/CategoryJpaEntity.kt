package com.commerce.common.persistence.category

import com.commerce.common.model.category.Category
import com.commerce.common.model.category.CategoryDetail
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "category")
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

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,
) {
    fun toModel(): Category = Category(
        id = id,
        parentId = parent?.id,
        name = name,
        depth = depth,
    )

    fun toProductModel(): CategoryDetail = CategoryDetail(
        id = id,
        name = name,
        parentCategory = parent?.let {
            CategoryDetail.ParentCategory(
                id = parent.id,
                name = parent.name,
            )
        }
    )
}