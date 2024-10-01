package com.commerce.service.order.controller.request

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrderSortOption
import com.commerce.common.model.orders.OrderStatus
import com.commerce.service.order.application.usecase.command.OrderListCommand
import com.commerce.service.order.application.usecase.exception.InvalidInputException
import com.commerce.service.order.controller.common.request.CommonRequest
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class OrderListRequest(
    @field:NotNull(message = "Date range is required")
    val dateRange: DateRange = DateRange.LAST_WEEK,
    val status: OrderStatus? = null,
    val sortBy: OrderSortOption = OrderSortOption.RECENT,
    val page: Int = 0,
    val size: Int = 20,
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 쿼리 파라미터 파싱
    var orderStartDate: LocalDate? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 쿼리 파라미터 파싱
    var orderEndDate: LocalDate? = null,
) : CommonRequest {

    // 주문 조회 날짜 범위
    // - LAST_WEEK: 지난 1주
    // - LAST_MONTH: 지난 1개월
    // - LAST_3_MONTHS: 지난 3개월
    // - LAST_6_MONTHS: 지난 6개월
    // - CUSTOM: 직접 지정
    enum class DateRange {
        LAST_WEEK, LAST_MONTH, LAST_3_MONTHS, LAST_6_MONTHS, CUSTOM;

        fun getStartToEnd(startDate: LocalDate?, endDate: LocalDate?): Pair<LocalDate, LocalDate> {
            val now = LocalDate.now()
            return when (this) {
                LAST_WEEK -> now.minusWeeks(1) to now
                LAST_MONTH -> now.minusMonths(1) to now
                LAST_3_MONTHS -> now.minusMonths(3) to now
                LAST_6_MONTHS -> now.minusMonths(6) to now
                CUSTOM -> {
                    if (startDate == null || endDate == null) {
                        throw InvalidInputException("Start date and end date are required for custom date range")
                    }
                    startDate to endDate
                }
            }
        }
    }

    init {
        validate()
    }

    override fun validate() {
        if (dateRange == DateRange.CUSTOM) {
            if (orderStartDate == null || orderEndDate == null) {
                throw InvalidInputException("Custom date range requires start and end dates")
            }
            if (!orderStartDate!!.isBefore(orderEndDate)) {
                throw InvalidInputException("Start date must be before end date")
            }
            if (!orderEndDate!!.isAfter(orderStartDate)) {
                throw InvalidInputException("End date must be after start date")
            }
        }
        if (page < 0) {
            throw InvalidInputException("Page must be non-negative")
        }
        if (size <= 0) {
            throw InvalidInputException("Size must be positive")
        }
    }

    fun toCommand(member: Member): OrderListCommand {
        val (orderDate, endDate) = dateRange.getStartToEnd(orderStartDate, orderEndDate)
        return OrderListCommand(
            member = member,
            orderDate = orderDate,
            endDate = endDate,
            status = status,
            sortBy = sortBy,
            page = page,
            size = size
        )
    }
}