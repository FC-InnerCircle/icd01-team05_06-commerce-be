package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest
import java.time.LocalDateTime

data class ExportOrdersRequest(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val format: String,
) : CommonRequest {
    override fun validate() {
        require(!startDate.isAfter(endDate)) { "Start date must be before or equal to end date" }
        require(format.isNotBlank()) { "Export format cannot be blank" }
    }
}
