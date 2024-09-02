package com.commerce.common.persistence.category

import com.commerce.common.model.category.Category
import com.commerce.common.model.category.CategoryRepository
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl(
    private val categoryJpaRepository: CategoryJpaRepository
) : CategoryRepository {
    override fun findAll(): List<Category> {
        return categoryJpaRepository.findAll().map { it.toModel() }
    }
}