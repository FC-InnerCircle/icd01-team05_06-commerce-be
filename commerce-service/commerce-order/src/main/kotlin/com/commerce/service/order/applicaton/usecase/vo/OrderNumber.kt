package com.commerce.service.order.applicaton.usecase.vo

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 주문번호 값 객체
@JvmInline
value class OrderNumber (val value: String) {
    // 주문번호는 ("ORD-20240815-001") 이와 같이 ORD-날짜-id 형태로 생성된다.
    // id를 전달받아서 생성한다.
    companion object {
        fun create(id: String): OrderNumber {
            return OrderNumber("ORD-${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))}-$id")
        }
    }
}