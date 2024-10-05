package com.commerce.service.product.application.service

import com.commerce.common.model.address.Address
import com.commerce.common.model.member.FakeMemberRepository
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.review.FakeReviewRepository
import com.commerce.common.model.review.Review
import com.commerce.common.model.review.ReviewRepository
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ReviewServiceTest {

    private lateinit var reviewRepository: ReviewRepository
    private lateinit var memberRepository: MemberRepository
    private val reviewService by lazy {
        ReviewService(reviewRepository, memberRepository)
    }

    @BeforeEach
    fun setUp() {
        memberRepository = FakeMemberRepository()
        reviewRepository = FakeReviewRepository(memberRepository)
    }

    @Test
    fun `상품의 리뷰를 조회한다`() {
        // given
        val newMember = Member(
            id = 1L,
            name = "김리뷰",
            email = "abc@naver.com",
            password = "1234",
            phone = "010123412341",
            address = Address(
                postalCode = "1234",
                streetAddress = "경동로123",
                detailAddress = "201동 12호",
            ),
        )
        memberRepository.save(newMember)
        reviewRepository.save(
            Review(
                productId = 1L,
                content = "정말 재미있어요",
                score = BigDecimal(5),
                memberId = 1L,
            )
        )

        // when
        val reviews = reviewService.getProductReviews(1L)

        // then
        assertThat(reviews).hasSize(1);
        val review = reviews[0]
        assertThat(review.id).isEqualTo(1L)
        assertThat(review.content).isEqualTo("정말 재미있어요")
        assertThat(review.score).isEqualTo(BigDecimal(5))
        assertThat(review.email).isEqualTo("abc@naver.com")
        assertThat(review.productId).isEqualTo(1L)
    }

    @Test
    fun `상품의 리뷰를 등록한다`() {

        // given
        val newMember = Member(
            id = 1L,
            name = "김리뷰",
            email = "abc@naver.com",
            password = "1234",
            phone = "010123412341",
            address = Address(
                postalCode = "1234",
                streetAddress = "경동로123",
                detailAddress = "201동 12호",
            ),
        )
        memberRepository.save(newMember)

        var addReviewCommand = AddReviewCommand(
            productId = 1L,
            email = "abc@naver.com",
            content = "재미있는 책이에요",
            score = BigDecimal(5),
            orderProductId = 1L,
        )
        reviewService.addReviewToProduct(addReviewCommand)

        val reviewsByProduct = reviewRepository.findByProductId(1L)

        assertThat(reviewsByProduct).hasSize(1)

        val reviewByProduct = reviewsByProduct[0]
        assertThat(reviewByProduct.productId).isEqualTo(1L)
        assertThat(reviewByProduct.email).isEqualTo("abc@naver.com")
        assertThat(reviewByProduct.content).isEqualTo("재미있는 책이에요")
        assertThat(reviewByProduct.score).isEqualTo(BigDecimal(5))
    }
}