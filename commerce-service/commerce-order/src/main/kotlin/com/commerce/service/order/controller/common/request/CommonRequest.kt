package com.commerce.service.order.controller.common.request

// 공통 요청 인터페이스
// - 모든 요청은 validate() 메서드를 통해 검증한다.
interface CommonRequest {
    fun validate()
}