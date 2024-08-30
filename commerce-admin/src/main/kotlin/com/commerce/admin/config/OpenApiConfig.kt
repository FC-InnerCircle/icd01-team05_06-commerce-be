package com.commerce.admin.config

import com.commerce.admin.application.usecase.annotation.SwaggerDoc
import com.commerce.admin.application.usecase.exception.ErrorCode
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod

@Configuration
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("이너서클 커머스 관리자 API")
                    .description("이너서클 커머스 관리자 API")
                    .version("v0.0.1")
                    .contact(
                        Contact()
                            .name("커머스 6조 백엔드파트 / 장현호")
                            .url("https://github.com/FC-InnerCircle/icd01-team05_06-commerce-be"),
                    ),
            ).components(
                Components()
                    .addSchemas(
                        "ApiResponse",
                        Schema<com.commerce.admin.application.usecase.dto.ApiResponse<*>>()
                            .type("object")
                            .addProperties("status", Schema<String>())
                            .addProperties("message", Schema<String>())
                            .addProperties("data", Schema<Any>())
                            .addProperties("timestamp", Schema<String>().format("date-time")),
                    ).addSchemas(
                        "ErrorResponse",
                        Schema<Any>()
                            .type("object")
                            .addProperties("code", Schema<String>())
                            .addProperties("message", Schema<String>())
                            .addProperties("status", Schema<Int>()),
                    ),
            )

    @Bean
    fun customOpenApiOperationCustomizer(): OperationCustomizer =
        OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            val customApiAnnotation = handlerMethod.getMethodAnnotation(SwaggerDoc::class.java)
            if (customApiAnnotation != null) {
                operation.summary(customApiAnnotation.summary)
                operation.description(customApiAnnotation.description)

                setRequestBody(operation, customApiAnnotation)
                setResponseBody(operation, customApiAnnotation.response200, "200")
                setResponseBody(operation, customApiAnnotation.response400, "400")
                setResponseBody(operation, customApiAnnotation.response404, "404")
                setResponseBody(operation, customApiAnnotation.response500, "500")
                setErrorResponses(operation)
            }
            operation
        }

    private fun setRequestBody(
        operation: Operation,
        annotation: SwaggerDoc,
    ) {
        if (annotation.requestClass != Unit::class) {
            val requestBody =
                operation.requestBody ?: io.swagger.v3.oas.models.parameters
                    .RequestBody()
            val content = requestBody.content ?: Content()
            val mediaType = content["application/json"] ?: MediaType()

            if (annotation.requestExample.isNotBlank()) {
                val example = Example().value(annotation.requestExample)
                mediaType.addExamples("default", example)
            }

            mediaType.schema(Schema<Any>().`$ref`("#/components/schemas/${annotation.requestClass.simpleName}"))
            content["application/json"] = mediaType
            requestBody.content = content
            operation.requestBody = requestBody
        }
    }

    private fun setResponseBody(
        operation: Operation,
        response: SwaggerDoc.Response,
        statusCode: String,
    ) {
        val apiResponses = operation.responses ?: ApiResponses()
        val apiResponse = apiResponses[statusCode] ?: ApiResponse().description(response.description)
        val content = Content()
        val mediaType = MediaType()

        if (response.responseClass != Unit::class) {
            if (statusCode == "200") {
                val schema =
                    Schema<Any>()
                        .`$ref`("#/components/schemas/ApiResponse")
                val dataSchema =
                    Schema<Any>()
                        .`$ref`("#/components/schemas/${response.responseClass.simpleName}")
                schema.addProperties("data", dataSchema)
                mediaType.schema(schema)
            } else {
                mediaType.schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
            }
        }

        if (response.example.isNotBlank()) {
            val example = Example().value(response.example)
            mediaType.addExamples("default", example)
        }

        if (response.errorCodes.isNotEmpty()) {
            for (errorCode in response.errorCodes) {
                val errorExample =
                    Example().value(
                        mapOf(
                            "code" to errorCode.code,
                            "message" to errorCode.message,
                            "status" to errorCode.httpStatus.value(),
                        ),
                    )
                mediaType.addExamples(errorCode.code, errorExample)
            }
        }

        content.addMediaType("application/json", mediaType)
        apiResponse.content = content
        apiResponses.addApiResponse(statusCode, apiResponse)
        operation.responses = apiResponses
    }

    private fun setErrorResponses(operation: Operation) {
        val apiResponses = operation.responses ?: ApiResponses()

        // Add common error responses
        addErrorResponse(apiResponses, ErrorCode.INVALID_INPUT)
        addErrorResponse(apiResponses, ErrorCode.RESOURCE_NOT_FOUND)
        addErrorResponse(apiResponses, ErrorCode.INTERNAL_SERVER_ERROR)

        // Add more specific error responses as needed
        addErrorResponse(apiResponses, ErrorCode.ORDER_NOT_FOUND)

        operation.responses = apiResponses
    }

    private fun addErrorResponse(
        apiResponses: ApiResponses,
        errorCode: ErrorCode,
    ) {
        val errorResponse = ApiResponse().description(errorCode.message)
        val content = Content()
        val mediaType = MediaType()

        val schema = Schema<Any>().`$ref`("#/components/schemas/ErrorResponse")
        mediaType.schema(schema)

        val example =
            Example().value(
                mapOf(
                    "code" to errorCode.code,
                    "message" to errorCode.message,
                    "status" to errorCode.httpStatus.value(),
                ),
            )
        mediaType.addExamples("default", example)

        content.addMediaType("application/json", mediaType)
        errorResponse.content = content
        apiResponses.addApiResponse(errorCode.httpStatus.value().toString(), errorResponse)
    }
}
