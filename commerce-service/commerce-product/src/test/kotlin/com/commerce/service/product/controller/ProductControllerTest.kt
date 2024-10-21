package com.commerce.service.product.controller

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.address.Address
import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.category.CategoryRepository
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.product.HomeProductType
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.product.SaleStatus
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.product.application.usecase.ProductUseCase
import com.commerce.service.product.application.usecase.dto.PaginationInfoDto
import com.commerce.service.product.application.usecase.dto.ProductCategoryInfoDto
import com.commerce.service.product.application.usecase.dto.ProductInfoDto
import com.commerce.service.product.application.usecase.dto.ProductPaginationInfoDto
import com.commerce.service.product.application.usecase.query.SelectQuery
import com.commerce.service.product.config.SecurityConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal
import java.time.LocalDate

@Import(ProductController::class, ObjectMapperConfig::class, JwtAuthenticationFilter::class)
@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
@WebMvcTest(controllers = [ProductController::class])
class ProductControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenUseCase: TokenUseCase

    @MockBean
    private lateinit var memberRepository: MemberRepository

    @MockBean
    private lateinit var productUseCase: ProductUseCase

    @MockBean
    private lateinit var productRepository: ProductRepository

    @MockBean
    private lateinit var categoryRepository: CategoryRepository

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
    }

    @Test
    fun getProductCategories() {
        given(productUseCase.getProductCategories()).willReturn(
            ProductCategoryInfoDto(
                childCategories = listOf(
                    ProductCategoryInfoDto.ProductCategoryChildInfoDto(
                        id = 1L,
                        parentId = 0L,
                        name = "한국도서",
                        depth = 1,
                        childCategories = listOf(
                            ProductCategoryInfoDto.ProductCategoryChildInfoDto(
                                id = 3L,
                                parentId = 1L,
                                name = "문학 소설",
                                depth = 2,
                                childCategories = null,
                            )
                        )
                    ),
                    ProductCategoryInfoDto.ProductCategoryChildInfoDto(
                        id = 2L,
                        parentId = 0L,
                        name = "외국도서",
                        depth = 1,
                        childCategories = listOf(
                            ProductCategoryInfoDto.ProductCategoryChildInfoDto(
                                id = 4L,
                                parentId = 2L,
                                name = "문학 소설",
                                depth = 2,
                                childCategories = null,
                            )
                        )
                    )
                )
            )
        )

        mockMvc.perform(
            get("/product/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "categories",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.childCategories").type(JsonFieldType.ARRAY).description("자식 카테고리")
                            .optional(),
                        fieldWithPath("data.childCategories[].id").type(JsonFieldType.NUMBER).description("자식 카테고리 id"),
                        fieldWithPath("data.childCategories[].parentId").type(JsonFieldType.NUMBER)
                            .description("자식 카테고리의 부모 카테고리 id"),
                        fieldWithPath("data.childCategories[].name").type(JsonFieldType.STRING)
                            .description("자식 카테고리 이름"),
                        fieldWithPath("data.childCategories[].depth").type(JsonFieldType.NUMBER)
                            .description("자식 카테고리의 뎁스"),
                        fieldWithPath("data.childCategories[].childCategories").type(JsonFieldType.ARRAY)
                            .description("더 하위의 자식 카테고리 리스트 (null일 수 있음)").optional(),
                        fieldWithPath("data.childCategories[].childCategories[].id").type(JsonFieldType.NUMBER)
                            .description("하위 자식 카테고리 id").optional(),
                        fieldWithPath("data.childCategories[].childCategories[].parentId").type(JsonFieldType.NUMBER)
                            .description("하위 자식 카테고리의 부모 카테고리 id").optional(),
                        fieldWithPath("data.childCategories[].childCategories[].name").type(JsonFieldType.STRING)
                            .description("하위 자식 카테고리 이름").optional(),
                        fieldWithPath("data.childCategories[].childCategories[].depth").type(JsonFieldType.NUMBER)
                            .description("하위 자식 카테고리의 뎁스").optional(),
                        fieldWithPath("data.childCategories[].childCategories[].childCategories").type(JsonFieldType.ARRAY)
                            .description("더 하위의 자식 카테고리 리스트 (null일 수 있음)").optional(),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun getProducts() {
        val query = SelectQuery(2, "국내도서", HomeProductType.HOT_NEW, BigDecimal("15000"), BigDecimal("20000"),1, 20)

        given(productUseCase.getProducts(query)).willReturn(
            ProductPaginationInfoDto(
                products = listOf(
                    ProductInfoDto(
                        id = 1L,
                        title = "[개발팀]국내도서 테스트 상품 (테스트 상품입니다.)",
                        author = "Rudolph Swift",
                        price = BigDecimal("29475"),
                        discountedPrice = BigDecimal("25317"),
                        publisher = "YES24",
                        publishDate = LocalDate.now(),
                        isbn = "1111111111",
                        description = "‘국가란 무엇인가’ 이후, ‘역사란 무엇인가’를 묻다\n\n유시민과 함께 역사의 갈피를 찾다!\n\n\n\n『거꾸로 읽는 세계사』로부터 30년, 작가 유시민 글쓰기의 새로운 시작. 헤로도토스의 『역사』, 투키디데스의 『펠로폰네소스 전쟁사』부터 유발 하라리의 『사피엔스』까지 고대로부터 최근까지 역사를 사로잡은 18권의 역사서들을 9장으로 나누어 훑으며 ‘역사’라는 화두를 전개해간다. 각 역사서의 주요 내용과 시대적인 맥락, 서사의 새로운 초점과 해석, 역사가의 생애 등을 유시민만의 언어로 요약했다.\n\n\n\n여기에 역사가의 속마음을 전달하고, 놓치지 말아야 할 부분을 체크해 주거나, 이해하지 못해도 좋다고 위로하고 격려하는 안내자 역할까지 맡았다. 역사에 대한 애정과 역사 공부의 중요성을 몸소 보여주며, 자신의 역사 공부법을 공개하는 셈이다.",
                        pages = 900,
                        coverImage = "https://shopping-phinf.pstatic.net/main_4964421/49644215637.20240806201630.jpg",
                        previewLink = "https://search.shopping.naver.com/book/catalog/49644215637",
                        stockQuantity = 90,
                        rating = 3.9,
                        status = SaleStatus.ON_SALE,
                        category = CategoryDetail(
                            id = 2,
                            name = "외국도서",
                            parentCategory = CategoryDetail.ParentCategory(
                                id = 0,
                                name = "전체"
                            )
                        )
                    ),
                    ProductInfoDto(
                        id = 2L,
                        title = "국내도서The Challenge of Greatness (The Legacy of Great Teachers)",
                        author = "Gose, Michael",
                        price = BigDecimal("31672"),
                        discountedPrice = BigDecimal("28778"),
                        publisher = "국내총판도서",
                        publishDate = LocalDate.now(),
                        isbn = "9781610480901",
                        description = "The Challenge of Greatness:  The Legacy of Great Teachers reveals the characteristics and teaching strategies of Great Teachers.  Simultaneously the book describes a Pantheon of thirty-two great teachers, and challenges the reader to continue their legacy by becoming one.  Recognizing the uniqueness of a great teacher, the book raises the kind of issues they face, and a range of possibilities from which they find solutions.",
                        pages = 140,
                        coverImage = "https://shopping-phinf.pstatic.net/main_3247428/32474284194.20220520074619.jpg",
                        previewLink = "https://search.shopping.naver.com/book/catalog/32474284194",
                        stockQuantity = 31,
                        rating = 4.1,
                        status = SaleStatus.ON_SALE,
                        category = CategoryDetail(
                            id = 2,
                            name = "외국도서",
                            parentCategory = CategoryDetail.ParentCategory(
                                id = 0,
                                name = "전체"
                            )
                        )
                    ),
                ),
                pagination = PaginationInfoDto(
                    pageSize = 20,
                    currentPage = 1,
                    totalPage = 1,
                    totalCount = 2,
                    hasNextPage = true,
                    hasPreviousPage = false,
                )
            )
        )

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams["productCategoryId"] = "2"
        queryParams["searchWord"] = "국내도서"
        queryParams["minPrice"] = "15000"
        queryParams["maxPrice"] = "20000"
        queryParams["page"] = "1"
        queryParams["size"] = "20"
        queryParams["homeProductType"] = HomeProductType.HOT_NEW.name

        mockMvc.perform(
            get("/product/v1/products")
                .params(queryParams)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "products",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("productCategoryId").description("카테고리 Id").optional(),
                        parameterWithName("searchWord").description("검색어").optional(),
                        parameterWithName("homeProductType").description("상품 타입(추천 도서 : RECOMMEND / 베스트 셀러 : BESTSELLER / 화제의 신간 : HOT_NEW)").optional(),
                        parameterWithName("minPrice").description("최소 금액").optional(),
                        parameterWithName("maxPrice").description("최대 금액").optional(),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("size").description("사이즈"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.products").type(JsonFieldType.ARRAY).description("상품 데이터"),
                        fieldWithPath("data.pagination").type(JsonFieldType.OBJECT).description("상품 데이터 페이징 정보"),

                        fieldWithPath("data.pagination.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.pagination.totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("data.pagination.pageSize").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                        fieldWithPath("data.pagination.totalCount").type(JsonFieldType.NUMBER).description("전체 갯수"),
                        fieldWithPath("data.pagination.hasNextPage").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                        fieldWithPath("data.pagination.hasPreviousPage").type(JsonFieldType.BOOLEAN).description("전 페이지 존재 여부"),

                        fieldWithPath("data.products[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                        fieldWithPath("data.products[].title").type(JsonFieldType.STRING).description("도서 제목"),
                        fieldWithPath("data.products[].author").type(JsonFieldType.STRING).description("작가"),
                        fieldWithPath("data.products[].price").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("data.products[].discountedPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                        fieldWithPath("data.products[].publisher").type(JsonFieldType.STRING).description("출판사"),
                        fieldWithPath("data.products[].publishDate").type(JsonFieldType.STRING).description("출판일"),
                        fieldWithPath("data.products[].isbn").type(JsonFieldType.STRING).description("isbn"),
                        fieldWithPath("data.products[].description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("data.products[].pages").type(JsonFieldType.NUMBER).description("페이지 수"),
                        fieldWithPath("data.products[].coverImage").type(JsonFieldType.STRING).description("커버 이미지"),
                        fieldWithPath("data.products[].previewLink").type(JsonFieldType.STRING).description("미리보기 링크"),
                        fieldWithPath("data.products[].stockQuantity").type(JsonFieldType.NUMBER).description("남은 수량"),
                        fieldWithPath("data.products[].rating").type(JsonFieldType.NUMBER).description("별점"),
                        fieldWithPath("data.products[].status").type(JsonFieldType.STRING).description("상태"),

                        fieldWithPath("data.products[].category").type(JsonFieldType.OBJECT).description("카테고리"),
                        fieldWithPath("data.products[].category.id").type(JsonFieldType.NUMBER).description("카테고리 id"),
                        fieldWithPath("data.products[].category.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data.products[].category.parentCategory").type(JsonFieldType.OBJECT)
                            .description("부모 카테고리"),
                        fieldWithPath("data.products[].category.parentCategory.id").type(JsonFieldType.NUMBER)
                            .description("부모 카테고리 id"),
                        fieldWithPath("data.products[].category.parentCategory.name").type(JsonFieldType.STRING)
                            .description("부모 카테고리 이름"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun getProductDetail() {

        val productId = 1L
        given(productUseCase.getProductDetail(productId)).willReturn(
            ProductInfoDto(
                id = 1L,
                title = "[개발팀]국내도서 테스트 상품 (테스트 상품입니다.)",
                author = "Rudolph Swift",
                price = BigDecimal("29475"),
                discountedPrice = BigDecimal("25317"),
                publisher = "YES24",
                publishDate = LocalDate.now(),
                isbn = "1111111111",
                description = "‘국가란 무엇인가’ 이후, ‘역사란 무엇인가’를 묻다\n\n유시민과 함께 역사의 갈피를 찾다!\n\n\n\n『거꾸로 읽는 세계사』로부터 30년, 작가 유시민 글쓰기의 새로운 시작. 헤로도토스의 『역사』, 투키디데스의 『펠로폰네소스 전쟁사』부터 유발 하라리의 『사피엔스』까지 고대로부터 최근까지 역사를 사로잡은 18권의 역사서들을 9장으로 나누어 훑으며 ‘역사’라는 화두를 전개해간다. 각 역사서의 주요 내용과 시대적인 맥락, 서사의 새로운 초점과 해석, 역사가의 생애 등을 유시민만의 언어로 요약했다.\n\n\n\n여기에 역사가의 속마음을 전달하고, 놓치지 말아야 할 부분을 체크해 주거나, 이해하지 못해도 좋다고 위로하고 격려하는 안내자 역할까지 맡았다. 역사에 대한 애정과 역사 공부의 중요성을 몸소 보여주며, 자신의 역사 공부법을 공개하는 셈이다.",
                pages = 900,
                coverImage = "https://shopping-phinf.pstatic.net/main_4964421/49644215637.20240806201630.jpg",
                previewLink = "https://search.shopping.naver.com/book/catalog/49644215637",
                stockQuantity = 90,
                rating = 3.9,
                status = SaleStatus.ON_SALE,
                category = CategoryDetail(
                    id = 2,
                    name = "외국도서",
                    parentCategory = CategoryDetail.ParentCategory(
                        id = 0,
                        name = "전체"
                    )
                )
            )
        )

        mockMvc.perform(get("/product/v1/products/{productId}", 1))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "productDetail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("productId").description("상품 ID")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상품 id"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("도서 제목"),
                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("작가"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("data.discountedPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("출판사"),
                        fieldWithPath("data.publishDate").type(JsonFieldType.STRING).description("출판일"),
                        fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("isbn"),
                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("설명"),
                        fieldWithPath("data.pages").type(JsonFieldType.NUMBER).description("페이지 수"),
                        fieldWithPath("data.coverImage").type(JsonFieldType.STRING).description("커버 이미지"),
                        fieldWithPath("data.previewLink").type(JsonFieldType.STRING).description("미리보기 링크"),
                        fieldWithPath("data.stockQuantity").type(JsonFieldType.NUMBER).description("남은 수량"),
                        fieldWithPath("data.rating").type(JsonFieldType.NUMBER).description("별점"),
                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("상태"),

                        fieldWithPath("data.category").type(JsonFieldType.OBJECT).description("카테고리"),
                        fieldWithPath("data.category.id").type(JsonFieldType.NUMBER).description("카테고리 id"),
                        fieldWithPath("data.category.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data.category.parentCategory").type(JsonFieldType.OBJECT).description("부모 카테고리"),
                        fieldWithPath("data.category.parentCategory.id").type(JsonFieldType.NUMBER)
                            .description("부모 카테고리 id"),
                        fieldWithPath("data.category.parentCategory.name").type(JsonFieldType.STRING)
                            .description("부모 카테고리 이름"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )

    }
}
