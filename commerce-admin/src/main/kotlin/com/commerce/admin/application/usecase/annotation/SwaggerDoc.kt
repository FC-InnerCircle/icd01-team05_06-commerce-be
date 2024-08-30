package com.commerce.admin.application.usecase.annotation

import com.commerce.admin.application.usecase.exception.ErrorCode
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SwaggerDoc(
    val summary: String = "",
    val description: String = "",
    val requestClass: KClass<*> = Unit::class,
    val requestExample: String = "",
    val response200: Response = Response(),
    val response400: Response = Response(),
    val response404: Response = Response(),
    val response500: Response = Response(),
) {
    annotation class Response(
        val description: String = "",
        val responseClass: KClass<*> = Unit::class,
        val example: String = "",
        val errorCodes: Array<ErrorCode> = [],
    )
}
