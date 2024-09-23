package com.commerce.common.model.util

data class PaginationModel<T>(
    val data: List<T>,
    val pagination: PaginationInfo
)