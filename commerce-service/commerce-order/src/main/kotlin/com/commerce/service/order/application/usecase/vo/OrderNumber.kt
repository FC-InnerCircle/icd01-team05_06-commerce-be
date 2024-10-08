package com.commerce.service.order.application.usecase.vo

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicLong

// 주문번호 값 객체
@JvmInline
value class OrderNumber (val value: String) {
    // 주문번호는 ("ORD-20240815-001") 이와 같이 ORD-날짜-id 형태로 생성된다.
    // id를 전달받아서 생성한다.
    companion object {
        private val counter = AtomicLong(0)
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        private var lastResetDate: LocalDate = LocalDate.now()

        fun create(id: String): OrderNumber {
            return OrderNumber("ORD-${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))}-$id")
        }

        // 실제 주문 번호 생성
        // 주문번호는 ("ORD-20240815-000001") 이와 같이 "ORD-날짜-6자리순차번호" 형태로 생성된다.
        fun createOrderNumber(): OrderNumber {
            checkAndResetCounterIfNeeded()
            val date = LocalDate.now().format(dateFormatter)
            val sequence = counter.incrementAndGet().toString().padStart(6, '0')
            return OrderNumber("ORD-$date-$sequence")
        }

        // 날짜가 변경되었는지 확인하고 필요한 경우 카운터를 리셋
        private fun checkAndResetCounterIfNeeded() {
            val currentDate = LocalDate.now()
            if (currentDate > lastResetDate) {
                counter.set(0)
                lastResetDate = currentDate
            }
        }
    }
}