package com.commerce.service.product.controller.request

interface BasePageRequest {
    val page: Int
    val size: Int
}