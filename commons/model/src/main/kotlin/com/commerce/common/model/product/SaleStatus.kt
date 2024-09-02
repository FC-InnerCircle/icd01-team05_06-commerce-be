package com.commerce.common.model.product

enum class SaleStatus(val desc: String, val descDetail: String) {
    WAIT("판매대기", "설정한 판매 기간 이전의 상품 상태"),
    ON_SALE("판매중", "판매 기간이 유효하고 재고수량이 1 이상인 상태"),
    CLOSE("판매종료", "판매 종료일이 지난 상품 상태"),
    OUT_OF_STOCK("품절", "판매 기간이내에 재고수량이 0인 경우"),
    SUSPENSION("판매중지", "판매 기간이나 재고수량과 관계없이 관리자나, 판매자에 의해 상품이 판매 중지된 상태"),
    PROHIBITION("판매금지", "관리자가 직권으로 판매금지한 상품 상태(판매자 설정 불가"),
}