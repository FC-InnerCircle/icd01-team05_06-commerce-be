package com.commerce.service.order.controller

import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.controller.common.responese.CommonResponse
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.response.OrderSummary
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(OrderController::class)
@AutoConfigureRestDocs
class OrderControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var orderUseCase: OrderUseCase

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var sampleRequest: OrderListRequest
    private lateinit var sampleResponse: OrderListResponse

    @BeforeEach
    fun setUp() {
        // 샘플 데이터 정의
        sampleRequest = OrderListRequest(
            dateRange = OrderListRequest.DateRange.LAST_6_MONTHS,
            status = null,
            sortBy = OrderListRequest.SortOption.RECENT,
            page = 0,
            size = 20,
        )

        sampleResponse = OrderListResponse(
            orders = listOf(
                OrderSummary(
                    id = "1",
                    orderDate = LocalDateTime.of(2024, 8, 15, 14, 30),
                    status = "COMPLETED",
                    totalAmount = 120.50,
                    customerName = "John Doe"
                ),
                OrderSummary(
                    id = "2",
                    orderDate = LocalDateTime.of(2024, 8, 16, 9, 45),
                    status = "PENDING",
                    totalAmount = 75.00,
                    customerName = "Jane Smith"
                )
            ),
            totalElements = 2L,
            totalPages = 1
        )

        // Mock 설정
        `when`(orderUseCase.getOrder(sampleRequest)).thenReturn(sampleResponse)
    }

    @Test
    fun `주문 목록을 반환해야 한다`() {
        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sampleRequest)))
            .andExpect(status().isOk)
            .andDo(document("get-orders",
                requestFields(
                    fieldWithPath("dateRange").description("Date range for order list"),
                    fieldWithPath("status").description("Order status").optional(),
                    fieldWithPath("sortBy").description("Sort option"),
                    fieldWithPath("page").description("Page number"),
                    fieldWithPath("size").description("Page size"),
                    fieldWithPath("startDate").description("Start date for custom date range").optional(),
                    fieldWithPath("endDate").description("End date for custom date range").optional()
                ),
                responseFields(
                    fieldWithPath("success").description("Whether the request was successful"),
                    fieldWithPath("data").description("Response data"),
                    fieldWithPath("data.orders").description("List of orders"),
                    fieldWithPath("data.orders[].id").description("Order ID"),
                    fieldWithPath("data.orders[].orderDate").description("Date and time of the order"),
                    fieldWithPath("data.orders[].status").description("Order status"),
                    fieldWithPath("data.orders[].totalAmount").description("Total amount of the order"),
                    fieldWithPath("data.orders[].customerName").description("Customer name"),
                    fieldWithPath("data.totalElements").description("Total number of orders"),
                    fieldWithPath("data.totalPages").description("Total number of pages"),
                    fieldWithPath("error").description("Error information").optional()
                )
            ))
    }
}
