package com.commerce.common.model.orders

import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 주문번호 값 객체
@JvmInline
value class OrderNumber(val value: String) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        // 실제 주문 번호 생성
        // 주문번호는 ("ORD-20240815-000001") 이와 같이 "ORD-날짜-6자리순차번호" 형태로 생성된다.
        fun createOrderNumber(orderNumberRepository: OrderNumberRepository): OrderNumber {
            val date = LocalDate.now()
            val sequence = orderNumberRepository.countIncrementAndGet(date).toString().padStart(6, '0')
            return OrderNumber("ORD-${date.format(dateFormatter)}-$sequence")
        }
    }
}