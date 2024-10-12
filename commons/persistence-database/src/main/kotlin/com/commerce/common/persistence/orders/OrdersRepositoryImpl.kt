package com.commerce.common.persistence.orders

import com.commerce.common.model.orderProduct.OrderProductWithInfo
import com.commerce.common.model.orders.*
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.util.PaginationModel
import com.commerce.common.persistence.util.toPaginationModel
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class OrdersRepositoryImpl(
    private val ordersJpaRepository: OrdersJpaRepository,
    private val productRepository: ProductRepository,
) : OrdersRepository {
    override fun findResultByIdAndMemberId(id: Long, memberId: Long): OrdersResult? {
        val orders = ordersJpaRepository.findByIdAndMemberId(id, memberId)?.toOrder() ?: return null
        return with(orders) {
            OrdersResult(
                id = id,
                memberId = memberId,
                orderNumber = orderNumber,
                orderer = ordererInfo,
                products = orderProducts.map {
                    OrderProductWithInfo(
                        product = productRepository.findById(it.productId),
                        orderProduct = it
                    )
                },
                deliveryInfo = deliveryInfo,
                paymentInfo = paymentInfo,
                orderStatus = status
            )
        }
    }

    override fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDate,
        endDate: LocalDate,
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
                memberId, orderDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), status, pageable
            )
        } else {
            ordersJpaRepository.findByMemberIdAndOrderDateBetween(
                memberId, orderDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), pageable
            )
        }

        return pageResult.toPaginationModel { it.toOrder() }
    }

    override fun save(orders: Orders): Orders {
        return ordersJpaRepository.save(orders.toJpaEntity()).toOrder()
    }
}