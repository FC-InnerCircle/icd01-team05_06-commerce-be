package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest
import java.time.LocalDateTime

data class GetOrderStatisticsRequest(
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
) : CommonRequest {
    override fun validate() {
        if (startDate != null && endDate != null) {
            require(!startDate.isAfter(endDate)) { "Start date must be before or equal to end date" }
        }
    }
}
