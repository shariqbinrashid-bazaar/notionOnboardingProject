package com.bazaar.api.template.controller

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class)
open class BaseControllerTest {

    fun configureMockMvcFor(controller: Any, documentationContextProvider: RestDocumentationContextProvider): MockMvc {
        return MockMvcBuilders.standaloneSetup(controller)
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(documentationContextProvider))
            .alwaysDo<StandaloneMockMvcBuilder>(document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(documentationContextProvider)
                .uris()
                .withScheme("http")
                .withHost("localhost")
                .withPort(5010))
            .build()
    }

}