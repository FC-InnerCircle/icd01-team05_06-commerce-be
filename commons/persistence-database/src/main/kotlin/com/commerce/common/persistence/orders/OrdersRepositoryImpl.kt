package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.*
import com.commerce.common.model.util.PaginationInfo
import com.commerce.common.model.util.PaginationModel
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import kotlin.math.ceil

@Repository
class OrdersRepositoryImpl (
    private val ordersJpaRepository: OrdersJpaRepository
) : OrdersRepository {

    override fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus?,
        page: Int,
        size: Int,
        sortOption: OrderSortOption
    ): PaginationModel<Orders> {
        val sort = when (sortOption) {
            OrderSortOption.RECENT -> Sort.by(Sort.Direction.DESC, "orderDate")
            OrderSortOption.ORDER_STATUS -> Sort.by("status", "orderDate")
            OrderSortOption.ALL -> Sort.unsorted()
        }
        val pageable = PageRequest.of(page, size, sort)

        val pageResult = if (status != null) {
            ordersJpaRepository.findByMemberIdAndOrderDateBetweenAndStatus(
                memberId, orderDate, endDate, status.toJpaStatus(), pageable
            )
        } else {
            ordersJpaRepository.findByMemberIdAndOrderDateBetween(
                memberId, orderDate, endDate, pageable
            )
        }
        val orders = pageResult.content.map { it.toOrder() }

        val totalPage = ceil(pageResult.totalElements.toDouble() / size).toInt()

        val paginationInfo = PaginationInfo(
            currentPage = page,
            totalCount = pageResult.totalElements,
            totalPage = totalPage,
            pageSize = size,
            hasNextPage = page < totalPage,
            hasPreviousPage = page > 1,
        )

        return PaginationModel(
            data = orders,
            pagination = paginationInfo
        )
    }
}