package com.commerce.admin.application.usecase.exception

import com.commerce.admin.application.usecase.dto.ApiResponse
import com.commerce.admin.application.usecase.dto.ErrorResponse

open class BaseException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.message,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause) {
    fun toApiResponse(customMessage: String? = null): ApiResponse<ErrorResponse> = ApiResponse.error(errorCode, customMessage ?: message)
}

// // 사용 예시
// fun exampleUsage() {
//    try {
//        // some code that might throw an exception
//        throw OrderNotFoundException("Order with ID 12345 not found")
//    } catch (e: BaseException) {
//        val response = e.toApiResponse("이부분 커스텀하게 변경 가능해?")
//        // response를 클라이언트에게 반환
//    }
// }

// {
//    "status": "ERROR",
//    "message": "Order with ID 12345 not found", //커스터 메시지 노출 지점
//    "data": {
//    "code": "O001",
//    "message": "Order not found",
//    "status": 404
// },
//    "timestamp": "2024-08-31T15:30:00Z"
// }
