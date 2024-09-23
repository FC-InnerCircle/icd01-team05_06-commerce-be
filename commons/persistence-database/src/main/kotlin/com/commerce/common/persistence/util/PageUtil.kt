package com.commerce.common.persistence.util

import com.commerce.common.model.util.PaginationInfo
import com.commerce.common.model.util.PaginationModel
import org.springframework.data.domain.Page

// 확장 함수 정의, transform을 추가하여 데이터 변환 가능하도록
fun <T, R> Page<T>.toPaginationModel(transform: (T) -> R): PaginationModel<R> {
    return PaginationModel(
        data = this.content.map(transform),  // transform 함수를 사용하여 매핑
        pagination = PaginationInfo(
            currentPage = this.number + 1,  // 현재 페이지 (0-based index이므로 +1)
            totalPage = this.totalPages,  // 전체 페이지 수
            pageSize = this.size,  // 페이지당 데이터 수
            totalCount = this.totalElements,  // 전체 데이터 수
            hasNextPage = this.hasNext(),  // 다음 페이지 여부
            hasPreviousPage = this.hasPrevious()  // 이전 페이지 여부
        )
    )
}