package com.bazaar.api.template.security.filter

import com.bazaar.api.common.constant.BazaarConstant
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.AdditionalMatchers
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.lang.Exception
import java.util.Arrays
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
internal class PreAuthorizationFilterTest {
    private var preAuthorizationFilter: PreAuthorizationFilter? = null
    @BeforeEach
    fun beforeEach() {
        preAuthorizationFilter = PreAuthorizationFilter()
    }

    @Nested
    internal inner class doFilterInternal {
        val USER_ID = "b42a435465"
        val SCOPES = Arrays.asList("scope-1", "scope-2")
        val UNAUTHORIZED_MESSAGE = "Invalid token"

        @Nested
        internal inner class flowTesting {
            @Mock
            private val mockRequest: HttpServletRequest? = null

            @Mock
            private val mockResponse: HttpServletResponse? = null

            @Mock
            private val mockFilterChain: FilterChain? = null

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = ["  ", "false", "FALSE", "some-invalid-value"])
            @Throws(
                Exception::class
            )
            fun WHEN_IsValidated_HAS_FalsyOrInvalidValue(isValidated: String?) {
                Mockito.`when`(mockRequest!!.getHeader(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE)))
                    .thenReturn(isValidated)
                preAuthorizationFilter!!.doFilter(mockRequest, mockResponse!!, mockFilterChain!!)
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE))
                Mockito.verify(mockRequest, Mockito.never())
                    .getHeader(AdditionalMatchers.not(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE)))
                Mockito.verify(mockResponse, Mockito.never())
                    .sendError(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
                Mockito.verify(mockFilterChain, Mockito.times(1))
                    .doFilter(ArgumentMatchers.eq(mockRequest), ArgumentMatchers.eq(mockResponse))
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "TRUE", "tRuE"])
            @Throws(
                Exception::class
            )
            fun WHEN_IsValidated_HAS_TruthyValue(isValidated: String) {
                Mockito.`when`(mockRequest!!.getHeader(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE)))
                    .thenReturn(isValidated)
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(BazaarConstant.USER_ID_ATTRIBUTE)))
                    .thenReturn(USER_ID)
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(BazaarConstant.USER_SCOPES_ATTRIBUTE)))
                    .thenReturn(
                        StringUtils.join(SCOPES, ",")
                    )
                preAuthorizationFilter!!.doFilter(mockRequest, mockResponse!!, mockFilterChain!!)
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE))
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(BazaarConstant.USER_ID_ATTRIBUTE))
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(BazaarConstant.USER_SCOPES_ATTRIBUTE))
                Mockito.verify(mockResponse, Mockito.never())
                    .sendError(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
                Mockito.verify(mockFilterChain, Mockito.times(1))
                    .doFilter(ArgumentMatchers.eq(mockRequest), ArgumentMatchers.eq(mockResponse))
            }

            @Test
            @Throws(Exception::class)
            fun WHEN_ExceptionOccurred() {
                Mockito.`when`(mockRequest!!.getHeader(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE)))
                    .thenThrow(
                        RuntimeException::class.java
                    )
                preAuthorizationFilter!!.doFilter(mockRequest, mockResponse!!, mockFilterChain!!)
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE))
                Mockito.verify(mockRequest, Mockito.never())
                    .getHeader(AdditionalMatchers.not(ArgumentMatchers.eq(BazaarConstant.IS_VALIDATED_ATTRIBUTE)))
                Mockito.verify(mockFilterChain, Mockito.never())
                    .doFilter(ArgumentMatchers.eq(mockRequest), ArgumentMatchers.eq(mockResponse))
                Mockito.verify(mockResponse, Mockito.times(1)).sendError(
                    ArgumentMatchers.eq(HttpStatus.UNAUTHORIZED.value()),
                    ArgumentMatchers.eq(UNAUTHORIZED_MESSAGE)
                )
            }
        }

        @Nested
        internal inner class resultTesting {
            private var httpServletRequest: MockHttpServletRequest? = null
            private var httpServletResponse: MockHttpServletResponse? = null
            private var filterChain: MockFilterChain? = null
            @BeforeEach
            fun beforeEach() {
                httpServletRequest = MockHttpServletRequest()
                httpServletResponse = MockHttpServletResponse()
                filterChain = MockFilterChain()
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "TRUE", "tRuE"])
            @Throws(
                Exception::class
            )
            fun WHEN_IsValidated_AND_AttributesExist(isValidated: String?) {
                httpServletRequest!!.addHeader(BazaarConstant.IS_VALIDATED_ATTRIBUTE, isValidated!!)
                httpServletRequest!!.addHeader(BazaarConstant.USER_ID_ATTRIBUTE, USER_ID)
                httpServletRequest!!.addHeader(BazaarConstant.USER_SCOPES_ATTRIBUTE, StringUtils.join(SCOPES, ","))
                preAuthorizationFilter!!.doFilter(httpServletRequest!!, httpServletResponse!!, filterChain!!)
                Assertions.assertEquals(HttpStatus.OK.value(), httpServletResponse!!.status)
            }

            @ParameterizedTest
            @ValueSource(strings = ["", " ", "false", "some-invalid-value"])
            @Throws(
                Exception::class
            )
            fun WHEN_IsNotValidated(isValidated: String?) {
                httpServletRequest!!.addHeader(BazaarConstant.IS_VALIDATED_ATTRIBUTE, isValidated!!)
                preAuthorizationFilter!!.doFilter(httpServletRequest!!, httpServletResponse!!, filterChain!!)
                Assertions.assertEquals(HttpStatus.OK.value(), httpServletResponse!!.status)
            }

            @Test
            @Throws(Exception::class)
            fun WHEN_ExceptionOccurred() {
                httpServletRequest!!.addHeader(BazaarConstant.IS_VALIDATED_ATTRIBUTE, true)
                preAuthorizationFilter!!.doFilter(httpServletRequest!!, httpServletResponse!!, filterChain!!)
                Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), httpServletResponse!!.status)
                Assertions.assertEquals(UNAUTHORIZED_MESSAGE, httpServletResponse!!.errorMessage)
            }
        }
    }
}