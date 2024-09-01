package com.commerce.service.order.controller.request

import com.commerce.service.order.controller.common.request.CommonRequest
import com.commerce.service.order.controller.common.request.DateRange
import com.commerce.service.order.controller.common.request.SortOption
import java.time.LocalDateTime

data class OrderListRequest(
    val dateRange: DateRange,
    val status: String? = null,
    val sortBy: SortOption = SortOption.RECENT,
    val page: Int = 0,
    val size: Int = 20
) : CommonRequest {

    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null

    override fun validate() {
        if (dateRange == DateRange.CUSTOM) {
            require(startDate != null && endDate != null) { "Custom date range requires start and end dates" }
            require(startDate!!.isBefore(endDate)) { "Start date must be before end date" }
        }
        require(page >= 0) { "Page must be non-negative" }
        require(size > 0) { "Size must be positive" }
    }
}