package com.commerce.service.auth.controller.common

data class BaseResponse(
    val message: String,
) {
    companion object {
        fun success() = BaseResponse("success")
    }
}
