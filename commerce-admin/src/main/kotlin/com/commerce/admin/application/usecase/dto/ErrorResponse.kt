package com.commerce.admin.application.usecase.dto

data class ErrorResponse(
    val code: String,
    val message: String,
    val status: Int,
)
