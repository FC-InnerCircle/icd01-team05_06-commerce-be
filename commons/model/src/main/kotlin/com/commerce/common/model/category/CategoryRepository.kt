package com.commerce.common.model.category

interface CategoryRepository {

    fun findAll(): List<Category>
}