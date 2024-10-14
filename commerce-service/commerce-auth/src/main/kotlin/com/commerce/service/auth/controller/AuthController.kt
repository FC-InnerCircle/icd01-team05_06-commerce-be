package com.commerce.service.auth.controller

import com.commerce.common.model.member.Member
import com.commerce.common.persistence.category.CategoryJpaEntity
import com.commerce.common.persistence.category.CategoryJpaRepository
import com.commerce.common.persistence.product.ProductJpaEntity
import com.commerce.common.persistence.product.ProductJpaRepository
import com.commerce.common.persistence.product.ProductTagJpaEntity
import com.commerce.common.persistence.product.ProductTagJpaRepository
import com.commerce.common.response.CommonResponse
import com.commerce.common.response.ErrorCode
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.MemberInfoDto
import com.commerce.service.auth.application.usecase.exception.AuthException
import com.commerce.service.auth.controller.request.LoginRequest
import com.commerce.service.auth.controller.request.PasswordVerifyRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
import com.commerce.service.auth.controller.response.PasswordVerifyResponse
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.ClassPathResource
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth/v1")
class AuthController(
    private val authUseCase: AuthUseCase,
    private val objectMapper: ObjectMapper,
    private val productJpaRepository: ProductJpaRepository,
    private val productTagJpaRepository: ProductTagJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): CommonResponse<LoginInfoDto> {
        return CommonResponse.ok(authUseCase.login(request.toCommand()))
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): CommonResponse<Unit> {
        authUseCase.signUp(request.toCommand())
        return CommonResponse.ok()
    }

    @GetMapping("/info")
    fun info(@AuthenticationPrincipal member: Member): CommonResponse<MemberInfoDto> {
        val memberInfo = MemberInfoDto(
            id = member.id,
            name = member.name,
            email = member.email,
            phone = member.phone,
            postalCode = member.address.postalCode,
            streetAddress = member.address.streetAddress,
            detailAddress = member.address.detailAddress
        )
        return CommonResponse.ok(memberInfo)
    }

    @PostMapping("/refresh")
    fun refresh(request: HttpServletRequest): CommonResponse<LoginInfoDto> {
        val authHeader = request.getHeader("refresh-token")
        if (authHeader?.startsWith("Bearer ") != true) {
            throw AuthException(ErrorCode.PERMISSION_ERROR)
        }

        val refreshToken = authHeader.substring(7)

        return CommonResponse.ok(authUseCase.refresh(refreshToken))
    }

    @PostMapping("/password-verify")
    fun passwordVerify(
        @AuthenticationPrincipal member: Member,
        @RequestBody request: PasswordVerifyRequest
    ): CommonResponse<PasswordVerifyResponse> {
        val token = authUseCase.passwordVerify(member, request.password)
        return CommonResponse.ok(PasswordVerifyResponse(token))
    }

    @PutMapping("/update")
    fun update(
        @AuthenticationPrincipal member: Member,
        @RequestHeader("auth-token") token: String,
        @RequestBody request: UpdateRequest
    ): CommonResponse<Unit> {
        authUseCase.update(member, token, request.toCommand())
        return CommonResponse.ok()
    }

    @DeleteMapping("/withdrawal")
    fun withdrawal(@AuthenticationPrincipal member: Member): CommonResponse<Unit> {
        authUseCase.withdrawal(member)
        return CommonResponse.ok()
    }

    @GetMapping("/json-product")
    fun jsonProduct() {
        val parent1 = categoryJpaRepository.save(CategoryJpaEntity(id = 1, name = "국내도서", depth = 1))
        val parent2 = categoryJpaRepository.save(CategoryJpaEntity(id = 2, name = "외국도서", depth = 1))
        val categoryList = listOf(
            CategoryJpaEntity(id = 3, name = "과학", depth = 2, parent = parent1),
            CategoryJpaEntity(id = 4, name = "과학", depth = 2, parent = parent2),
            CategoryJpaEntity(id = 5, name = "소설", depth = 2, parent = parent1),
            CategoryJpaEntity(id = 6, name = "소설", depth = 2, parent = parent2),
            CategoryJpaEntity(id = 7, name = "시", depth = 2, parent = parent1),
            CategoryJpaEntity(id = 8, name = "시", depth = 2, parent = parent2),
            CategoryJpaEntity(id = 9, name = "역사", depth = 2, parent = parent1),
            CategoryJpaEntity(id = 10, name = "역사", depth = 2, parent = parent2),
        )
        categoryList.forEach { categoryJpaRepository.save(it) }

        ClassPathResource("products.json").inputStream.use { inputStream ->
            val bytes = FileCopyUtils.copyToByteArray(inputStream)
            val json = String(bytes)
            val list = objectMapper.readValue(json, object : TypeReference<List<Map<String, Any>>>() {})
            list.forEachIndexed { index, map ->
                val item = objectMapper.writeValueAsString(map)

                val tags = map["tags"] as List<String>
                tags.forEach {
                    productTagJpaRepository.save(
                        ProductTagJpaEntity(
                            name = it,
                            productId = map["id"] as Long
                        )
                    )
                }

                val categoryMap = map["category"] as Map<String, Any>
                val parentCategoryMap = categoryMap["parentCategory"] as Map<String, Any>
                val parentCategoryId = parentCategoryMap["id"] as Long
                val categoryName = categoryMap["name"] as String

                val book = objectMapper.readValue(item, ProductJpaEntity::class.java)
                book.categoryId = categoryList.find { it.name == categoryName && it.parent!!.id == parentCategoryId }!!.id
                book.isHotNew = index < 30 && index % 3 == 0
                book.isRecommend = index < 30 && index % 3 == 1
                book.isBestseller = index < 30 && index % 3 == 2
                productJpaRepository.save(book)
            }
        }
    }
}