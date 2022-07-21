package com.bazaar.api.template.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(MockitoExtension::class)
class TemplateControllerTest : BaseControllerTest() {


    private lateinit var mockMvc: MockMvc

    companion object {
        const val BASE_URI = "/v1/template"
    }

    @BeforeEach
    fun beforeEach(documentationContextProvider: RestDocumentationContextProvider) {
        mockMvc = super.configureMockMvcFor(TemplateController(), documentationContextProvider)
    }


    @Nested
    inner class GetTemplate {

        @Test
        fun withGoodPayload() {

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$BASE_URI")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            Assertions.assertEquals(andReturn.response.status, HttpStatus.OK.value())
            Assertions.assertEquals(andReturn.response.contentAsString, "Template")
        }
    }
}