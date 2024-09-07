package com.commerce.service.order.controller

import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.*
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(OrderController::class)
@AutoConfigureRestDocs
class OrdersControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var orderUseCase: OrderUseCase

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var sampleListRequest: OrderListRequest
    private lateinit var sampleListResponse: OrderListResponse
    private lateinit var sampleDetailResponse: OrderDetailResponse

    @BeforeEach
    fun setUp() {
        // 주문 목록 샘플 데이터
        sampleListRequest = OrderListRequest(
            dateRange = OrderListRequest.DateRange.LAST_6_MONTHS,
            status = null,
            sortBy = OrderListRequest.SortOption.RECENT,
            page = 0,
            size = 20,
        )

        sampleListResponse = OrderListResponse(
            products = listOf(
                OrderSummary(
                    id = "1",
                    orderNumber = "ORD-20240815-001",
                    content = "해리 포터와 마법사의 돌 외 2권",
                    orderDate = LocalDateTime.of(2024, 8, 15, 14, 30).toString(),
                    status = "배송완료",
                    pricie = 45000.0,
                    discoutedPrice = 40500.0,
                    memberName = "김철수",
                    recipient = "김영희"
                ),
                OrderSummary(
                    id = "2",
                    orderNumber = "ORD-20240816-002",
                    content = "코스모스: 가능한 세계들",
                    orderDate = LocalDateTime.of(2024, 8, 16, 9, 45).toString(),
                    status = "결제완료",
                    pricie = 22000.0,
                    discoutedPrice = 19800.0,
                    memberName = "이지은",
                    recipient = "이지은"
                )
            ),
            totalElements = 2L,
            totalPages = 1
        )

        // 주문 상세 샘플 데이터
        sampleDetailResponse = OrderDetailResponse(
            order = OrderDetail(
                id = "1",
                orderNumber = "ORD-20240815-001",
                orderDate = LocalDateTime.of(2024, 8, 15, 14, 30),
                status = "배송완료",
                totalAmount = 45000.0,
                customerName = "김철수",
                shippingAddress = "서울특별시 강남구 테헤란로 123 우리집 아파트 101동 1001호",
                paymentMethod = "신용카드"
            ),
            items = listOf(
                OrderProduct(
                    id = 1,
                    orderId = 1,
                    productId = 101,
                    quantity = 1,
                    price = BigDecimal("15000.00"),
                    discountedPrice = BigDecimal("13500.00"),
                    createdAt = LocalDateTime.of(2024, 8, 15, 14, 30),
                    updatedAt = LocalDateTime.of(2024, 8, 15, 14, 30)
                ),
                OrderProduct(
                    id = 2,
                    orderId = 1,
                    productId = 102,
                    quantity = 1,
                    price = BigDecimal("15000.00"),
                    discountedPrice = BigDecimal("13500.00"),
                    createdAt = LocalDateTime.of(2024, 8, 15, 14, 30),
                    updatedAt = LocalDateTime.of(2024, 8, 15, 14, 30)
                ),
                OrderProduct(
                    id = 3,
                    orderId = 1,
                    productId = 103,
                    quantity = 1,
                    price = BigDecimal("15000.00"),
                    discountedPrice = BigDecimal("13500.00"),
                    createdAt = LocalDateTime.of(2024, 8, 15, 14, 30),
                    updatedAt = LocalDateTime.of(2024, 8, 15, 14, 30)
                )
            ),
            statusHistory = listOf(
                StatusHistoryItem(
                    status = "주문접수",
                    timestamp = LocalDateTime.of(2024, 8, 15, 14, 30)
                ),
                StatusHistoryItem(
                    status = "결제완료",
                    timestamp = LocalDateTime.of(2024, 8, 15, 14, 35)
                ),
                StatusHistoryItem(
                    status = "배송준비중",
                    timestamp = LocalDateTime.of(2024, 8, 15, 16, 0)
                ),
                StatusHistoryItem(
                    status = "배송중",
                    timestamp = LocalDateTime.of(2024, 8, 16, 9, 0)
                ),
                StatusHistoryItem(
                    status = "배송완료",
                    timestamp = LocalDateTime.of(2024, 8, 17, 14, 0)
                )
            )
        )

        // Mock 설정
        `when`(orderUseCase.getOrder(sampleListRequest)).thenReturn(sampleListResponse)
        `when`(orderUseCase.getOrderDetail("1")).thenReturn(sampleDetailResponse)
    }

    @Test
    fun `주문 목록을 반환해야 한다`() {
        mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sampleListRequest)))
            .andExpect(status().isOk)
            .andDo(document("get-orders",
                requestFields(
                    fieldWithPath("dateRange").description("주문 목록 조회 기간"),
                    fieldWithPath("status").description("주문 상태").optional(),
                    fieldWithPath("sortBy").description("정렬 옵션"),
                    fieldWithPath("page").description("페이지 번호"),
                    fieldWithPath("size").description("페이지 크기"),
                    fieldWithPath("startDate").description("사용자 지정 조회 시작일").optional(),
                    fieldWithPath("endDate").description("사용자 지정 조회 종료일").optional()
                ),
                responseFields(
                    fieldWithPath("success").description("요청 성공 여부"),
                    fieldWithPath("data").description("응답 데이터"),
                    fieldWithPath("data.products").description("주문 목록"),
                    fieldWithPath("data.products[].id").description("주문 ID"),
                    fieldWithPath("data.products[].orderNumber").description("주문 번호"),
                    fieldWithPath("data.products[].content").description("주문 내용"),
                    fieldWithPath("data.products[].orderDate").description("주문 일시"),
                    fieldWithPath("data.products[].status").description("주문 상태"),
                    fieldWithPath("data.products[].pricie").description("주문 금액"),
                    fieldWithPath("data.products[].discoutedPrice").description("할인된 주문 금액"),
                    fieldWithPath("data.products[].memberName").description("주문자 이름"),
                    fieldWithPath("data.products[].recipient").description("수령인 이름"),
                    fieldWithPath("data.totalElements").description("전체 주문 수"),
                    fieldWithPath("data.totalPages").description("전체 페이지 수"),
                    fieldWithPath("error").description("오류 정보").optional()
                )
            ))
    }

    @Test
    fun `주문 상세 정보를 반환해야 한다`() {
        mockMvc.perform(get("/orders/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo(document("get-order-detail",
                responseFields(
                    fieldWithPath("success").description("요청 성공 여부"),
                    fieldWithPath("data").description("응답 데이터"),
                    fieldWithPath("data.order").description("주문 상세 정보"),
                    fieldWithPath("data.order.id").description("주문 ID"),
                    fieldWithPath("data.order.orderNumber").description("주문 번호"),
                    fieldWithPath("data.order.orderDate").description("주문 일시"),
                    fieldWithPath("data.order.status").description("주문 상태"),
                    fieldWithPath("data.order.totalAmount").description("총 주문 금액"),
                    fieldWithPath("data.order.customerName").description("주문자 이름"),
                    fieldWithPath("data.order.shippingAddress").description("배송 주소"),
                    fieldWithPath("data.order.paymentMethod").description("결제 방법"),
                    fieldWithPath("data.items").description("주문 상품 목록"),
                    fieldWithPath("data.items[].id").description("주문 상품 ID"),
                    fieldWithPath("data.items[].orderId").description("주문 ID"),
                    fieldWithPath("data.items[].productId").description("상품 ID"),
                    fieldWithPath("data.items[].quantity").description("주문 수량"),
                    fieldWithPath("data.items[].price").description("상품 가격"),
                    fieldWithPath("data.items[].discountedPrice").description("할인된 상품 가격"),
                    fieldWithPath("data.items[].createdAt").description("생성 일시"),
                    fieldWithPath("data.items[].updatedAt").description("수정 일시"),
                    fieldWithPath("data.statusHistory").description("주문 상태 이력"),
                    fieldWithPath("data.statusHistory[].status").description("주문 상태"),
                    fieldWithPath("data.statusHistory[].timestamp").description("상태 변경 시간"),
                    fieldWithPath("error").description("오류 정보").optional()
                )
            ))
    }
}