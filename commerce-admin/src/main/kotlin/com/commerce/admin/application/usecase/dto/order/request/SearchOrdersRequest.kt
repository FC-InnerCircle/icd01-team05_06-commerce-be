package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest
import java.time.LocalDateTime

data class SearchOrdersRequest(
    val query: String? = null,
    val status: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val minAmount: Double? = null,
    val maxAmount: Double? = null,
    val page: Int = 1,
    val limit: Int = 20,
) : CommonRequest {
    override fun validate() {
        require(page > 0) { "Page must be greater than 0" }
        require(limit > 0) { "Limit must be greater than 0" }
        if (minAmount != null && maxAmount != null) {
            require(minAmount <= maxAmount) { "Minimum amount must be less than or equal to maximum amount" }
        }
        if (startDate != null && endDate != null) {
            require(!startDate.isAfter(endDate)) { "Start date must be before or equal to end date" }
        }
    }
}
