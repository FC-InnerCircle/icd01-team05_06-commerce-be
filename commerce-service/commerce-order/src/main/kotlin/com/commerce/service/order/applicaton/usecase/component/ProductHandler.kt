package com.commerce.service.order.applicaton.usecase.component

/**
 * ProductHandler
 * - 상품 정보 조회
 * - 재고 확인
 * - 고객 정보 확인
 * - 주문 생성
 * - 재고 업데이트
 * - 주문 완료 처리
 */
interface ProductHandler {
    fun getProductInfo()
    fun checkStock()
    fun getCustomerInfo()
    fun createOrder()
    fun updateStock()
    fun completeOrder()
}