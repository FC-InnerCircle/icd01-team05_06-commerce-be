package com.commerce.common.response

data class CommonResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: List<ErrorResponse>? = null,
) {
    companion object {
        fun <T> ok(data: T? = null): CommonResponse<T> {
            return CommonResponse(
                success = true,
                data = data
            )
        }

        fun error(errors: List<ErrorResponse>): CommonResponse<Unit> {
            return CommonResponse(
                success = false,
                error = errors
            )
        }
    }
}