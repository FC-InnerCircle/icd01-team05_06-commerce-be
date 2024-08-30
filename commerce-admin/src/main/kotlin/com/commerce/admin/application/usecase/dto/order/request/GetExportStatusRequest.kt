package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest

data class GetExportStatusRequest(
    val exportId: String,
) : CommonRequest {
    override fun validate() {
        require(exportId.isNotBlank()) { "Export ID cannot be blank" }
    }
}
