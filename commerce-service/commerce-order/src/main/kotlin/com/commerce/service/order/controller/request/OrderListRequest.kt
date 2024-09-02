package com.commerce.service.order.controller.request

import com.commerce.service.order.controller.common.request.CommonRequest
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class OrderListRequest(
    @field:NotNull(message = "Date range is required")
    val dateRange: DateRange,
    val status: String? = null,
    val sortBy: SortOption = SortOption.RECENT,
    val page: Int = 0,
    val size: Int = 20,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var startDate: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var endDate: LocalDateTime? = null
) : CommonRequest {

    // 주문 조회 날짜 범위
    // - LAST_WEEK: 지난 1주
    // - LAST_MONTH: 지난 1개월
    // - LAST_3_MONTHS: 지난 3개월
    // - LAST_6_MONTHS: 지난 6개월
    // - CUSTOM: 직접 지정
    enum class DateRange {
        LAST_WEEK, LAST_MONTH, LAST_3_MONTHS, LAST_6_MONTHS, CUSTOM
    }

    // 주문 조회 정렬 옵션
    // - RECENT: 최신순
    // - ORDER_STATUS: 주문 상태별
    // - ALL: 전체
    enum class SortOption {
        RECENT, ORDER_STATUS, ALL
    }

    override fun validate() {
        if (dateRange == DateRange.CUSTOM) {
            require(startDate != null && endDate != null) { "Custom date range requires start and end dates" }
            require(startDate!!.isBefore(endDate)) { "Start date must be before end date" }
        }
        require(page >= 0) { "Page must be non-negative" }
        require(size > 0) { "Size must be positive" }
    }
}