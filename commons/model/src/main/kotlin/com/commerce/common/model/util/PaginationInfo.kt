package com.commerce.common.model.util

data class PaginationInfo(
    val currentPage: Int,
    val totalPage: Int,
    val pageSize: Int,
    val totalCount: Long,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
)