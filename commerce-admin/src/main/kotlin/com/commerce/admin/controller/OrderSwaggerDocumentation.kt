@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.commerce.admin.controller

import com.commerce.admin.application.usecase.annotation.SwaggerDoc
import com.commerce.admin.application.usecase.dto.ApiResponse
import com.commerce.admin.application.usecase.dto.order.request.*
import com.commerce.admin.application.usecase.dto.order.response.*
import com.commerce.admin.application.usecase.exception.ErrorCode
import org.springframework.http.ResponseEntity

interface OrderSwaggerDocumentation {
    @SwaggerDoc(
        summary = "Get orders list",
        description = "Retrieve a list of orders with pagination and optional filtering",
        requestClass = GetOrdersRequest::class,
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Orders retrieved successfully",
              "data": {
                "orders": [
                  {
                    "id": "ord_12345",
                    "customerName": "John Doe",
                    "totalAmount": 100.50,
                    "status": "PROCESSING",
                    "createdAt": "2024-08-27T10:30:00Z"
                  }
                ],
                "total": 100,
                "page": 1,
                "limit": 20
              },
              "timestamp": "2024-08-27T10:35:00Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun getOrders(request: GetOrdersRequest): ResponseEntity<ApiResponse<OrderListResponse>>

    @SwaggerDoc(
        summary = "Get order details",
        description = "Retrieve detailed information about a specific order",
        requestClass = Unit::class,
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Order details retrieved successfully",
              "data": {
                "id": "ord_12345",
                "customerName": "John Doe",
                "email": "john@example.com",
                "totalAmount": 100.50,
                "status": "PROCESSING",
                "items": [
                  {
                    "productId": "prod_789",
                    "name": "Sample Product",
                    "quantity": 2,
                    "price": 50.25
                  }
                ]
              },
              "timestamp": "2024-08-27T10:35:00Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun getOrderDetails(orderId: String): ResponseEntity<ApiResponse<OrderDetails>>

    @SwaggerDoc(
        summary = "Export orders",
        description = "Request an export of orders within a specified date range",
        requestClass = ExportOrdersRequest::class,
        requestExample = """
        {
          "startDate": "2024-07-01T00:00:00Z",
          "endDate": "2024-07-31T23:59:59Z",
          "format": "CSV"
        }
        """,
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Order export initiated successfully",
              "data": {
                "exportId": "exp_9876",
                "status": "PROCESSING",
                "estimatedCompletionTime": "2024-08-27T17:30:00Z"
              },
              "timestamp": "2024-08-27T10:35:00Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun exportOrders(request: ExportOrdersRequest): ResponseEntity<ApiResponse<ExportOrdersResponse>>

    @SwaggerDoc(
        summary = "Update order status",
        description = "Update the status of a specific order",
        requestClass = UpdateOrderStatusRequest::class,
        requestExample = """{"status": "SHIPPED"}""",
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Order status updated successfully",
              "data": {
                "id": "ord_12345",
                "status": "SHIPPED",
                "updatedAt": "2024-08-27T15:45:00Z"
              },
              "timestamp": "2024-08-27T15:45:05Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun updateOrderStatus(
        orderId: String,
        request: UpdateOrderStatusRequest,
    ): ResponseEntity<ApiResponse<UpdateStatusResponse>>

    @SwaggerDoc(
        summary = "Cancel order",
        description = "Cancel a specific order",
        requestClass = CancelOrderRequest::class,
        requestExample = """{"reason": "Customer request"}""",
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Order cancelled successfully",
              "data": {
                "id": "ord_12345",
                "status": "CANCELLED",
                "cancelledAt": "2024-08-27T16:00:00Z",
                "reason": "Customer request"
              },
              "timestamp": "2024-08-27T16:00:05Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun cancelOrder(
        orderId: String,
        request: CancelOrderRequest,
    ): ResponseEntity<ApiResponse<CancelOrderResponse>>

    @SwaggerDoc(
        summary = "Search orders",
        description = "Search for orders based on various criteria",
        requestClass = SearchOrdersRequest::class,
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Orders searched successfully",
              "data": {
                "orders": [
                  {
                    "id": "ord_12345",
                    "customerName": "John Doe",
                    "totalAmount": 100.50,
                    "status": "PROCESSING",
                    "createdAt": "2024-08-27T10:30:00Z"
                  }
                ],
                "total": 1,
                "page": 1,
                "limit": 20
              },
              "timestamp": "2024-08-27T10:35:00Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun searchOrders(request: SearchOrdersRequest): ResponseEntity<ApiResponse<OrderListResponse>>

    @SwaggerDoc(
        summary = "Get order statistics",
        description = "Retrieve statistical information about orders",
        requestClass = GetOrderStatisticsRequest::class,
        response200 =
            SwaggerDoc.Response(
                description = "Successful response",
                responseClass = ApiResponse::class,
                example = """
            {
              "status": "SUCCESS",
              "message": "Order statistics retrieved successfully",
              "data": {
                "totalOrders": 1000,
                "totalRevenue": 100000.00,
                "averageOrderValue": 100.00
              },
              "timestamp": "2024-08-27T11:00:00Z"
            }
            """,
            ),
        response404 =
            SwaggerDoc.Response(
                description = "Order not found",
                errorCodes = [ErrorCode.ORDER_NOT_FOUND],
            ),
        response400 =
            SwaggerDoc.Response(
                description = "Bad request",
                errorCodes = [ErrorCode.INVALID_INPUT],
            ),
        response500 =
            SwaggerDoc.Response(
                description = "Internal server error",
                errorCodes = [ErrorCode.INTERNAL_SERVER_ERROR],
            ),
    )
    fun getOrderStatistics(request: GetOrderStatisticsRequest): ResponseEntity<ApiResponse<OrderStatistics>>
}
