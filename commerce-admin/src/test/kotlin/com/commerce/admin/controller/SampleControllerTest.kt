package com.commerce.admin.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class SampleControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getSampleEndpoint() {
        this.mockMvc
            .perform(get("/api/sample"))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "sample",
                    responseFields(
                        fieldWithPath("message").description("A sample message"),
                    ),
                ),
            )
    }
}
