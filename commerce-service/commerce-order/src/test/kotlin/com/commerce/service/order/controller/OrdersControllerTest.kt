package com.commerce.service.order.controller

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.address.Address
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.model.orderProduct.OrderProductWithInfo
import com.commerce.common.model.orders.*
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.SaleStatus
import com.commerce.common.model.util.PaginationInfo
import com.commerce.common.restdocs.RestDocsUtil
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.order.application.usecase.OrderUseCase
import com.commerce.service.order.config.SecurityConfig
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.response.OrderSummary
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
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
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
    private lateinit var sampleResult: OrdersResult

    private val testAccessToken =
        "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzI0NTIwNDc5LCJleHAiOjE3MjU3MzAwNzl9.i1WjcNXU2wBYjikGu5u0r41XmciafAfaMF3nNheb9cc7TUpai-tnMZCg3NUcTWP9"
    private val testMember = Member(
        id = 1,
        email = "commerce@example.com",
        password = "123!@#qwe",
        name = "홍길동",
        phone = "01012345678",
        address = Address(
            postalCode = "12345",
            streetAddress = "서울 종로구 테스트동",
            detailAddress = "123-45"
        )
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
            orderStartDate = null,
            orderEndDate = null
        )

        sampleListResponse = OrderListResponse(
            products = listOf(
                OrderSummary(
                    id = 1,
                    orderNumber = OrderNumber("ORD-20240815-000001"),
                    content = "해리 포터와 마법사의 돌 외 2권",
                    orderDate = LocalDateTime.of(2024, 8, 15, 14, 30).toString(),
                    status = OrderStatus.SHIPPING,
                    price = 45000.0,
                    discountedPrice = 40500.0,
                    memberName = "김철수",
                    recipient = "김영희"
                ),
                OrderSummary(
                    id = 2,
                    orderNumber = OrderNumber("ORD-20240815-000002"),
                    content = "코스모스: 가능한 세계들",
                    orderDate = LocalDateTime.of(2024, 8, 16, 9, 45).toString(),
                    status = OrderStatus.COMPLETED,
                    price = 22000.0,
                    discountedPrice = 19800.0,
                    memberName = "이지은",
                    recipient = "이지은"
                )
            ),
            paginationInfo = PaginationInfo(
                currentPage = 0,
                totalPage = 1,
                pageSize = 20,
                totalCount = 2L,
                hasNextPage = false,
                hasPreviousPage = false
            )
        )

        // 주문 상세 샘플 데이터
        sampleResult = OrdersResult(
            id = 6,
            memberId = 1,
            orderNumber = OrderNumber("ORD-20241012-000005"),
            price = BigDecimal("31672"),
            discountedPrice = BigDecimal("28778"),
            orderDate = LocalDateTime.now(),
            orderer = OrdererInfo(
                name = "주문자2",
                phoneNumber = "01012345002",
                email = "chadwick.madden@example.com"
            ),
            products = listOf(
                OrderProductWithInfo(
                    orderProduct = OrderProduct(
                        id = 2,
                        orderId = 6,
                        productId = 3,
                        quantity = 1,
                        price = BigDecimal("31672"),
                        discountedPrice = BigDecimal("28778")
                    ), product = Product(
                        id = 3,
                        title = "The Challenge of Greatness (The Legacy of Great Teachers)",
                        author = "Gose, Michael",
                        price = BigDecimal("31672"),
                        discountedPrice = BigDecimal("28778"),
                        publisher = "국내총판도서",
                        publishDate = LocalDateTime.now(),
                        isbn = "9781610480901",
                        description = "설명~~",
                        pages = 123,
                        coverImage = "https://shopping-phinf.pstatic.net/main_3247428/32474284194.20220520074619.jpg",
                        previewLink = "https://search.shopping.naver.com/book/catalog/32474284194",
                        stockQuantity = 33,
                        rating = 4.5,
                        status = SaleStatus.CLOSE,
                        category = null,
                        isHotNew = false,
                        isRecommend = false,
                        isBestseller = false
                    )
                )
            ),
            deliveryInfo = DeliveryInfo(
                recipient = "수령인2",
                phoneNumber = "01054321002",
                address = Address(
                    postalCode = "12002",
                    streetAddress = "서울 종로구 종로3가 2",
                    detailAddress = "종로아파트 2호",
                ),
                memo = "배송메모2"
            ),
            paymentInfo = PaymentInfo(
                method = "CREDIT_CARD",
                depositorName = "예금주2"
            ),
            orderStatus = OrderStatus.COMPLETED
        )

        // Mock 설정
        `when`(orderUseCase.getOrder(sampleListRequest.toCommand(testMember))).thenReturn(sampleListResponse)
        `when`(orderUseCase.getOrderResult(testMember, OrderNumber("ORD-20241012-000001"))).thenReturn(sampleResult)
    }

    @Test
    fun `주문 목록을 반환해야 한다`() {
        mockMvc.perform(
            get("/order/v1/orders")
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
                        parameterWithName("dateRange").description("주문 목록 조회 기간").optional()
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
                        parameterWithName("status").description("주문 상태").optional().attributes(RestDocsUtil.format(OrderStatus.entries.joinToString(", ") { "$it : ${it.title}" })),
                        parameterWithName("sortBy").description("정렬 옵션").optional()
                            .attributes(key("format").value("ENUM (RECENT, ORDER_STATUS, ALL)"))
                            .attributes(
                                key("description").value(
                                    """
                        RECENT: 최근 생성된 주문부터 정렬
                        ORDER_STATUS: 주문 상태별로 정렬 후, 각 상태 내에서 생성 일시로 정렬
                        ALL: 특별한 정렬 기준 없음 (데이터베이스 기본 순서)
                    """.trimIndent())),
                        parameterWithName("page").description("페이지 번호 (0부터 시작)").optional()
                            .attributes(key("format").value("NUMBER (0 이상의 정수)"))
                            .attributes(key("description").value("조회하고자 하는 페이지 번호. 0부터 시작하며, 음수 입력 시 에러 발생")),
                        parameterWithName("size").description("페이지 크기").optional()
                            .attributes(key("format").value("NUMBER (양의 정수)"))
                            .attributes(key("description").value("한 페이지에 표시할 주문의 수. 1 이상의 정수여야 하며, 기본값은 20")),
                        parameterWithName("orderStartDate").description("사용자 지정 조회 시작일 (dateRange가 CUSTOM일 때 필수)").optional()
                            .attributes(key("format").value("yyyy-MM-dd"))
                            .attributes(key("description").value("dateRange가 CUSTOM일 때 조회 시작 일자")),
                        parameterWithName("orderEndDate").description("사용자 지정 조회 종료일 (dateRange가 CUSTOM일 때 필수)").optional()
                            .attributes(key("format").value("yyyy-MM-dd"))
                            .attributes(key("description").value("dateRange가 CUSTOM일 때 조회 종료 일자. orderStartDate보다 미래여야 함"))
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
                            .attributes(RestDocsUtil.format(OrderStatus.entries.joinToString(", ") { "$it : ${it.title}" })),
                        fieldWithPath("data.products[].price").description("주문 원가")
                            .attributes(key("format").value("Number (Double)")),
                        fieldWithPath("data.products[].discountedPrice").description("할인 적용된 최종 가격")
                            .attributes(key("format").value("Number (Double)")),
                        fieldWithPath("data.products[].memberName").description("주문자 이름")
                            .attributes(key("format").value("String")),
                        fieldWithPath("data.products[].recipient").description("수령인 이름")
                            .attributes(key("format").value("String")),
                        fieldWithPath("data.paginationInfo").description("페이지네이션 정보"),
                        fieldWithPath("data.paginationInfo.currentPage").description("현재 페이지 번호")
                            .attributes(key("format").value("Number (Integer)")),
                        fieldWithPath("data.paginationInfo.totalPage").description("전체 페이지 수")
                            .attributes(key("format").value("Number (Integer)")),
                        fieldWithPath("data.paginationInfo.pageSize").description("페이지 크기")
                            .attributes(key("format").value("Number (Integer)")),
                        fieldWithPath("data.paginationInfo.totalCount").description("전체 주문 수")
                            .attributes(key("format").value("Number (Long)")),
                        fieldWithPath("data.paginationInfo.hasNextPage").description("다음 페이지 존재 여부")
                            .attributes(key("format").value("Boolean")),
                        fieldWithPath("data.paginationInfo.hasPreviousPage").description("이전 페이지 존재 여부")
                            .attributes(key("format").value("Boolean")),
                        fieldWithPath("error").description("오류 정보").optional()
                            .attributes(key("format").value("Object (null if success)"))
                    )
            )
        )
    }

    @Test
    fun `주문 상세 정보를 반환해야 한다`() {
        mockMvc.perform(
            get("/order/v1/orders/{orderNumber}", "ORD-20241012-000001")
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
                        parameterWithName("orderNumber").description("주문 번호")
                    ),
                    responseFields(
                        fieldWithPath("success").description("요청 성공 여부"),
                        fieldWithPath("data").description("응답 데이터"),
                        fieldWithPath("data.id").description("주문 ID"),
                        fieldWithPath("data.orderNumber").description("주문 번호"),
                        fieldWithPath("data.orderDate").description("주문 일시"),
                        fieldWithPath("data.orderer").description("주문자 정보"),
                        fieldWithPath("data.orderer.name").description("이름"),
                        fieldWithPath("data.orderer.phoneNumber").description("연락처"),
                        fieldWithPath("data.orderer.email").description("이메일"),
                        fieldWithPath("data.products").description("주문 상품 목록"),
                        fieldWithPath("data.products[].id").description("상품 ID"),
                        fieldWithPath("data.products[].title").description("상품 이름"),
                        fieldWithPath("data.products[].author").description("상품 저자"),
                        fieldWithPath("data.products[].publisher").description("상품 출판사"),
                        fieldWithPath("data.products[].coverImage").type(JsonFieldType.STRING).description("상품 이미지"),
                        fieldWithPath("data.products[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.products[].price").type(JsonFieldType.NUMBER).description("상품 금액"),
                        fieldWithPath("data.products[].discountedPrice").type(JsonFieldType.NUMBER).description("상품 할인된 금액"),
                        fieldWithPath("data.deliveryInfo").description("배송 정보"),
                        fieldWithPath("data.deliveryInfo.recipient").description("수령인"),
                        fieldWithPath("data.deliveryInfo.phoneNumber").description("수령인 전화번호"),
                        fieldWithPath("data.deliveryInfo.streetAddress").description("도로명 주소"),
                        fieldWithPath("data.deliveryInfo.detailAddress").description("상세 주소"),
                        fieldWithPath("data.deliveryInfo.postalCode").description("우편번호"),
                        fieldWithPath("data.deliveryInfo.memo").optional().description("배송메모"),
                        fieldWithPath("data.paymentInfo").description("결제 정보"),
                        fieldWithPath("data.paymentInfo.method").description("결제 방법"),
                        fieldWithPath("data.paymentInfo.depositorName").description("입금자 이름"),
                        fieldWithPath("data.paymentInfo.price").type(JsonFieldType.NUMBER).description("주문 금액"),
                        fieldWithPath("data.paymentInfo.discountedPrice").type(JsonFieldType.NUMBER).description("주문 할인된 금액"),
                        fieldWithPath("data.orderStatus").description("주문 상태")
                            .attributes(RestDocsUtil.format(OrderStatus.entries.joinToString(", ") { "$it : ${it.title}" })),
                        fieldWithPath("error").description("오류 정보").optional()
                    )
                )
            )
    }

   @Test
    fun `주문 생성을 한다`() {
        // given
        val request = OrderCreateRequest(
            products = listOf(
                OrderCreateRequest.ProductInfo(id = 1, quantity = 2),
                OrderCreateRequest.ProductInfo(id = 3, quantity = 1)
            ),
            ordererInfo = OrderCreateRequest.OrdererInfo(
                name = "김오더",
                phoneNumber = "01022334455",
                email = "test@emaill.com"
            ),
            deliveryInfo = OrderCreateRequest.DeliveryInfo(
                recipient = "홍길동",
                phoneNumber = "01012345678",
                streetAddress = "서울시 강남구 테헤란로 123",
                detailAddress = "104동 1201호",
                postalCode = "12345",
                memo = "배송메모"
            ),
            paymentInfo = OrderCreateRequest.PaymentInfo(
                method = "CREDIT_CARD",
                depositorName = "홍길동"
            ),
            agreementInfo = OrderCreateRequest.AgreementInfo(
                termsOfService = true,
                privacyPolicy = true,
                ageVerification = true
            )
        )

       val response = OrderCreateResponse(
           id = 1,
           orderNumber = "ORD-20240815-001",
           orderStatus = "PENDING",
           orderDate = LocalDateTime.now().toString(),
           products = listOf(
               OrderCreateResponse.ProductSummary(
                   id = 1,
                   name = "Kotlin in Action",
                   quantity = 2,
                   price = BigDecimal(20000),
                   discountedPrice = BigDecimal(19000),
               ),
               OrderCreateResponse.ProductSummary(
                   id = 3,
                   name = "Spring Boot in Practice",
                   quantity = 1,
                   price = BigDecimal(10000),
                   discountedPrice = BigDecimal(9000),
               )

           )
       )

        // JwtAuthenticationFilter
        given(
            tokenUseCase.getTokenSubject(
                testAccessToken,
                TokenType.ACCESS_TOKEN
            )
        ).willReturn(testMember.id.toString())
        given(memberRepository.findById(testMember.id)).willReturn(testMember)

        `when`(orderUseCase.order(request.toCommand(testMember))).thenReturn(response)

       // when & then
       mockMvc.post("/order/v1/orders") {
           header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
           contentType = MediaType.APPLICATION_JSON
           content = objectMapper.writeValueAsString(request)
       }.andExpect {
           status { isOk() }
       }.andDo {
           handle(
               document(
                   "create-order",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   requestFields(
                       fieldWithPath("products").description("주문할 상품 목록"),
                       fieldWithPath("products[].id").description("상품 ID"),
                       fieldWithPath("products[].quantity").description("주문 수량"),
                       fieldWithPath("ordererInfo").description("주문자 정보"),
                       fieldWithPath("ordererInfo.name").description("이름"),
                       fieldWithPath("ordererInfo.phoneNumber").description("연락처"),
                       fieldWithPath("ordererInfo.email").description("이메일"),
                       fieldWithPath("deliveryInfo").description("배송 정보"),
                       fieldWithPath("deliveryInfo.recipient").description("수령인"),
                       fieldWithPath("deliveryInfo.phoneNumber").description("수령인 전화번호"),
                       fieldWithPath("deliveryInfo.streetAddress").description("도로명 주소"),
                       fieldWithPath("deliveryInfo.detailAddress").description("상세 주소"),
                       fieldWithPath("deliveryInfo.postalCode").description("우편번호"),
                       fieldWithPath("deliveryInfo.memo").optional().description("배송메모"),
                       fieldWithPath("paymentInfo").description("결제 정보"),
                       fieldWithPath("paymentInfo.method").description("결제 방법"),
                       fieldWithPath("paymentInfo.depositorName").description("입금자 이름"),
                       fieldWithPath("agreementInfo").description("동의 정보"),
                       fieldWithPath("agreementInfo.termsOfService").description("이용 약관 동의 여부"),
                       fieldWithPath("agreementInfo.privacyPolicy").description("개인정보 처리방침 동의 여부"),
                       fieldWithPath("agreementInfo.ageVerification").description("연령 확인 동의 여부")
                   ),
                   responseFields(
                       fieldWithPath("success").description("요청 성공 여부"),
                       fieldWithPath("data").description("응답 데이터"),
                       fieldWithPath("data.id").description("주문 아이디"),
                       fieldWithPath("data.orderNumber").description("주문 번호"),
                       fieldWithPath("data.orderStatus").description("주문 상태"),
                       fieldWithPath("data.orderDate").description("주문 일시"),
                       fieldWithPath("data.products").description("주문한 상품 목록"),
                       fieldWithPath("data.products[].id").description("상품 ID"),
                       fieldWithPath("data.products[].name").description("상품 이름"),
                       fieldWithPath("data.products[].quantity").description("주문 수량"),
                       fieldWithPath("data.products[].price").description("상품 가격"),
                       fieldWithPath("data.products[].discountedPrice").description("상품 할인된 가격"),
                       fieldWithPath("error").description("오류 정보").optional()
                   )
               )
           )
       }
   }
}