package com.commerce.service.product.application.usecase.dto

import com.commerce.common.model.util.PaginationInfo

data class PaginationInfoDto(
    val currentPage: Int,
    val totalPage: Int,
    val pageSize: Int,
    val totalCount: Long,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
) {
    companion object {
        fun from(pagination: PaginationInfo): PaginationInfoDto {
            return PaginationInfoDto(
                currentPage = pagination.currentPage,
                totalPage = pagination.totalPage,
                pageSize = pagination.pageSize,
                totalCount = pagination.totalCount,
                hasNextPage = pagination.hasNextPage,
                hasPreviousPage = pagination.hasPreviousPage,
            )
        }
    }
}