package com.commerce.admin.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SampleController {

    @GetMapping("/sample")
    @Operation(summary = "Get a sample", description = "Returns a sample message")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successful operation",
            content = [Content(schema = Schema(implementation = SampleResponse::class))])
    ])
    fun getSample(): ResponseEntity<SampleResponse> {
        return ResponseEntity.ok(SampleResponse("This is a sample message"))
    }
}

data class SampleResponse(val message: String)