package com.commerce.service.order.controller

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.model.orders.OrderSortOption
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.applicaton.usecase.domain.OrderNumber
import com.commerce.service.order.config.SecurityConfig
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal
import java.time.LocalDateTime

@Import(OrderController::class, ObjectMapperConfig::class, JwtAuthenticationFilter::class)
@WebMvcTest(OrderController::class)
@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
class OrdersControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenUseCase: TokenUseCase

    @MockBean
    private lateinit var memberRepository: MemberRepository

    @MockBean
    private lateinit var orderUseCase: OrderUseCase

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var sampleListRequest: OrderListRequest
    private lateinit var sampleListResponse: OrderListResponse
    private lateinit var sampleDetailResponse: OrderDetailResponse

    private val testAccessToken =
        "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzI0NTIwNDc5LCJleHAiOjE3MjU3MzAwNzl9.i1WjcNXU2wBYjikGu5u0r41XmciafAfaMF3nNheb9cc7TUpai-tnMZCg3NUcTWP9"
    private val testMember = Member(
        id = 1,
        email = "commerce@example.com",
        password = "123!@#qwe",
        name = "홍길동",
        phone = "01012345678"
    )

    @BeforeEach
    fun setUp(
        applicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()

        // JwtAuthenticationFilter
        given(
            tokenUseCase.getTokenSubject(
                testAccessToken,
                TokenType.ACCESS_TOKEN
            )
        ).willReturn(testMember.id.toString())
        given(memberRepository.findById(testMember.id)).willReturn(testMember)

        // 주문 목록 샘플 데이터
        sampleListRequest = OrderListRequest(
            dateRange = OrderListRequest.DateRange.LAST_6_MONTHS,
            status = null,
            sortBy = OrderSortOption.RECENT,
            page = 0,
            size = 20,
            orderDate = null,
            endDate = null
        )

        sampleListResponse = OrderListResponse(
            products = listOf(
                OrderSummary(
                    id = "1",
                    orderNumber = OrderNumber.create("1"),
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
                    orderNumber = OrderNumber.create("2"),
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
        `when`(orderUseCase.getOrder(sampleListRequest, testMember)).thenReturn(sampleListResponse)
        `when`(orderUseCase.getOrderDetail("1")).thenReturn(sampleDetailResponse)
    }

    @Test
    fun `주문 목록을 반환해야 한다`() {
        mockMvc.perform(
            get("/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .param("dateRange", sampleListRequest.dateRange.toString())
                .param("sortBy", sampleListRequest.sortBy.toString())
                .param("page", sampleListRequest.page.toString())
                .param("size", sampleListRequest.size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "get-orders",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("dateRange").description("주문 목록 조회 기간")
                            .attributes(key("format").value("ENUM (LAST_WEEK, LAST_MONTH, LAST_3_MONTHS, LAST_6_MONTHS, CUSTOM)"))
                            .attributes(
                                key("description").value(
                                    """
                            LAST_WEEK: 최근 1주일 동안의 주문
                            LAST_MONTH: 최근 1개월 동안의 주문
                            LAST_3_MONTHS: 최근 3개월 동안의 주문
                            LAST_6_MONTHS: 최근 6개월 동안의 주문
                            CUSTOM: 사용자 지정 기간 (startDate와 endDate 파라미터 필요)
                        """.trimIndent())),
                    parameterWithName("status").description("주문 상태").optional()
                        .attributes(key("format").value("ENUM (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUND, EXCHANGE)"))
                        .attributes(key("description").value("""
                            PENDING: 주문 생성
                            PROCESSING: 주문 처리중
                            SHIPPED: 배송중
                            DELIVERED: 배송완료
                            CANCELLED: 주문 취소
                            REFUND: 환불
                            EXCHANGE: 교환
                            (미지정 시 모든 상태의 주문 조회)
                        """.trimIndent()
                                )
                            ),
                        parameterWithName("sortBy").description("정렬 옵션")
                            .attributes(key("format").value("ENUM (RECENT, ORDER_STATUS, ALL)"))
                            .attributes(
                                key("description").value(
                                    """
                            RECENT: 최근 생성된 주문부터 정렬
                            ORDER_STATUS: 주문 상태별로 정렬 후, 각 상태 내에서 생성 일시로 정렬
                            ALL: 특별한 정렬 기준 없음 (데이터베이스 기본 순서)
                        """.trimIndent())),
                    parameterWithName("page").description("페이지 번호 (0부터 시작)")
                        .attributes(key("format").value("NUMBER (0 이상의 정수)"))
                        .attributes(key("description").value("조회하고자 하는 페이지 번호. 0부터 시작하며, 음수 입력 시 에러 발생")),
                    parameterWithName("size").description("페이지 크기")
                        .attributes(key("format").value("NUMBER (양의 정수)"))
                        .attributes(key("description").value("한 페이지에 표시할 주문의 수. 1 이상의 정수여야 하며, 기본값은 20")),
                    parameterWithName("orderDate").description("사용자 지정 조회 시작일 (dateRange가 CUSTOM일 때 필수)").optional()
                        .attributes(key("format").value("yyyy-MM-dd"))
                        .attributes(key("description").value("dateRange가 CUSTOM일 때 조회 시작 일자")),
                    parameterWithName("endDate").description("사용자 지정 조회 종료일 (dateRange가 CUSTOM일 때 필수)").optional()
                        .attributes(key("format").value("yyyy-MM-dd"))
                        .attributes(key("description").value("dateRange가 CUSTOM일 때 조회 종료 일자. orderDate보다 미래여야 함"))
                ),
                responseFields(
                    fieldWithPath("success").description("요청 성공 여부")
                        .attributes(key("format").value("Boolean")),
                    fieldWithPath("data").description("응답 데이터"),
                    fieldWithPath("data.products").description("주문 목록")
                        .attributes(key("format").value("Array")),
                    fieldWithPath("data.products[].id").description("주문 ID")
                        .attributes(key("format").value("String (UUID)")),
                    fieldWithPath("data.products[].orderNumber").description("주문 번호")
                        .attributes(key("format").value("String")),
                    fieldWithPath("data.products[].content").description("주문 내역 요약")
                        .attributes(key("format").value("String")),
                    fieldWithPath("data.products[].orderDate").description("주문 일자")
                        .attributes(key("format").value("String (ISO 8601: yyyy-MM-dd'T'HH:mm:ss)")),
                    fieldWithPath("data.products[].status").description("주문 상태")
                        .attributes(key("format").value("String (ENUM: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCEL, REFUND, EXCHANGE)")),
                    fieldWithPath("data.products[].pricie").description("주문 원가")
                        .attributes(key("format").value("Number (Double)")),
                    fieldWithPath("data.products[].discoutedPrice").description("할인 적용된 최종 가격")
                        .attributes(key("format").value("Number (Double)")),
                    fieldWithPath("data.products[].memberName").description("주문자 이름")
                        .attributes(key("format").value("String")),
                    fieldWithPath("data.products[].recipient").description("수령인 이름")
                        .attributes(key("format").value("String")),
                    fieldWithPath("data.totalElements").description("전체 주문 수")
                        .attributes(key("format").value("Number (Long)")),
                    fieldWithPath("data.totalPages").description("전체 페이지 수")
                        .attributes(key("format").value("Number (Integer)")),
                    fieldWithPath("error").description("오류 정보").optional()
                        .attributes(key("format").value("Object (null if success)"))
                )
            )
        )
    }

    @Test
    fun `주문 상세 정보를 반환해야 한다`() {
        mockMvc.perform(
            get("/orders/{orderId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "get-order-detail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("orderId").description("주문 ID")
                    ),
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
                )
            )
    }
}