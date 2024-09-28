package com.commerce.service.order.applicaton.service

import com.commerce.common.model.address.Address
import com.commerce.common.model.member.Member
import com.commerce.common.model.shopping_cart.ShoppingCart
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import com.commerce.service.order.applicaton.usecase.command.PatchShoppingCartCommand
import com.commerce.service.order.applicaton.usecase.command.PostShoppingCartCommand
import com.mock.common.model.shopping_cart.FakeShoppingCartRepository
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShoppingCartServiceTest {

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private val shoppingCartService by lazy {
        ShoppingCartService(shoppingCartRepository)
    }

    @BeforeEach
    fun setUp() {
        shoppingCartRepository = FakeShoppingCartRepository()
    }

    @Test
    fun `장바구니에 동일한 상품이 없으면 추가한다`() {
        val member = Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = "phasellus",
            name = "Cameron Mayo",
            phone = "(737) 231-4205",
            address = Address(
                postalCode = "12345",
                streetAddress = "서울 종로구 테스트동",
                detailAddress = "123-45"
            )

        )
        val command = PostShoppingCartCommand(
            productId = 2,
            quantity = 3
        )

        shoppingCartService.post(member, command);

        val result = shoppingCartRepository.findByMemberIdAndProductId(1, 2)
        assertThat(result).isNotNull
        assertThat(result!!.memberId).isEqualTo(1)
        assertThat(result.productId).isEqualTo(2)
        assertThat(result.quantity).isEqualTo(3)
    }

    @Test
    fun `장바구니에 동일한 상품이 있으면 수량을 더한다`() {
        shoppingCartRepository.save(
            ShoppingCart(
                memberId = 1,
                productId = 2,
                quantity = 4
            )
        )

        val member = Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = "phasellus",
            name = "Cameron Mayo",
            phone = "(737) 231-4205",
            address = Address(
                postalCode = "12345",
                streetAddress = "서울 종로구 테스트동",
                detailAddress = "123-45"
            )
        )
        val command = PostShoppingCartCommand(
            productId = 2,
            quantity = 3
        )

        shoppingCartService.post(member, command);

        val result = shoppingCartRepository.findByMemberIdAndProductId(1, 2)
        assertThat(result).isNotNull
        assertThat(result!!.memberId).isEqualTo(1)
        assertThat(result.productId).isEqualTo(2)
        assertThat(result.quantity).isEqualTo(7)
    }

    @Test
    fun `장바구니의 수량을 변경할 수 있다`() {
        val shoppingCart = shoppingCartRepository.save(
            ShoppingCart(
                memberId = 1,
                productId = 2,
                quantity = 4
            )
        )
        val command = PatchShoppingCartCommand(
            quantity = 5
        )

        shoppingCartService.patch(
            shoppingCartId = shoppingCart.id,
            command = command
        )

        val result = shoppingCartRepository.findById(shoppingCart.id)
        assertThat(result).isNotNull
        assertThat(result!!.quantity).isEqualTo(5)
    }

    @Test
    fun `장바구니의 상품을 삭제할 수 있다`() {
        val shoppingCart = shoppingCartRepository.save(
            ShoppingCart(
                memberId = 1,
                productId = 2,
                quantity = 4
            )
        )

        shoppingCartService.delete(shoppingCart.id)

        val result = shoppingCartRepository.findById(shoppingCart.id)
        assertThat(result).isNull()
    }
}