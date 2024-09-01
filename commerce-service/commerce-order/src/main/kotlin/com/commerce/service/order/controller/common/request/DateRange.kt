package com.commerce.service.order.controller.common.request

// 주문 조회 기간
// - LAST_WEEK: 지난 주
// - LAST_MONTH: 지난 달
// - LAST_3_MONTHS: 지난 3개월
// - LAST_6_MONTHS: 지난 6개월
// - CUSTOM: 직접 지정
enum class DateRange {
    LAST_WEEK, LAST_MONTH, LAST_3_MONTHS, LAST_6_MONTHS, CUSTOM
}