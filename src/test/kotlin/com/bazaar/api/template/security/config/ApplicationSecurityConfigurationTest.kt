package com.bazaar.api.template.security.config

import com.bazaar.api.template.security.filter.PreAuthorizationFilter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.config.annotation.SecurityBuilder
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@ExtendWith(MockitoExtension::class)
internal class ApplicationSecurityConfigurationTest {
    private var securityConfig: ApplicationSecurityConfiguration? = null

    @Mock
    private val preAuthorizationFilter: PreAuthorizationFilter = PreAuthorizationFilter()
    @BeforeEach
    fun beforeEach() {
        securityConfig = ApplicationSecurityConfiguration(preAuthorizationFilter)
    }

    @Nested
    internal inner class springSecurityConfiguration {


        val httpSecurity : HttpSecurity = Mockito.mock(HttpSecurity::class.java)

        @Mock
        private val corsConfigurer: CorsConfigurer<HttpSecurity>? = null

        @Mock
        private val csrfConfigurer: CsrfConfigurer<HttpSecurity>? = null

        @Mock
        val authorizedUrl: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl? = null

        @Mock
        private val expressionInterceptUrlRegistry: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry? = null

        @Mock
        private val sessionManagementConfigurer: SessionManagementConfigurer<HttpSecurity>? = null

        @Mock
        private val exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>? = null
        @Test
        @Throws(Exception::class)
        fun configureHttpSecurity() {
            // CORS should be configured
            Mockito.`when`(httpSecurity.cors()).thenReturn(corsConfigurer)
            Mockito.`when`<SecurityBuilder<*>?>(corsConfigurer!!.and()).thenReturn(httpSecurity)

            // CSRF should be configured to be disabled
            Mockito.`when`(httpSecurity.csrf()).thenReturn(csrfConfigurer)
            Mockito.`when`(csrfConfigurer!!.disable()).thenReturn(httpSecurity)

            Mockito.`when`(httpSecurity.authorizeRequests()).thenReturn(expressionInterceptUrlRegistry)
            if (expressionInterceptUrlRegistry != null) {
                Mockito.`when`<Any?>(expressionInterceptUrlRegistry.anyRequest()).thenReturn(authorizedUrl)
            }
            if (authorizedUrl != null) {
                Mockito.`when`<ExpressionUrlAuthorizationConfigurer<*>.ExpressionInterceptUrlRegistry>(authorizedUrl.permitAll())
                    .thenReturn(expressionInterceptUrlRegistry)
            }
            if (expressionInterceptUrlRegistry != null) {
                Mockito.`when`<HttpSecurityBuilder<*>?>(expressionInterceptUrlRegistry.and()).thenReturn(httpSecurity)
            }

            // SessionManagement should be configured with STATELESS policy
            Mockito.`when`(httpSecurity.sessionManagement()).thenReturn(sessionManagementConfigurer)
            Mockito.`when`(sessionManagementConfigurer!!.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .thenReturn(sessionManagementConfigurer)
            Mockito.`when`<SecurityBuilder<*>?>(sessionManagementConfigurer.and()).thenReturn(httpSecurity)

            // ExceptionHandling should be configured
            Mockito.`when`(httpSecurity.exceptionHandling()).thenReturn(exceptionHandlingConfigurer)
            Mockito.`when`(
                exceptionHandlingConfigurer!!.authenticationEntryPoint(
                    ArgumentMatchers.any(
                        HttpStatusEntryPoint::class.java
                    )
                )
            ).thenReturn(exceptionHandlingConfigurer)
            Mockito.`when`<SecurityBuilder<*>?>(exceptionHandlingConfigurer.and()).thenReturn(httpSecurity)
            securityConfig?.configure(httpSecurity)
            Mockito.verify(httpSecurity, Mockito.times(1)).cors()
            Mockito.verify(httpSecurity, Mockito.times(1)).csrf()
            Mockito.verify(csrfConfigurer, Mockito.times(1)).disable()
            Mockito.verify(httpSecurity, Mockito.times(1)).authorizeRequests()
            Mockito.verify<ExpressionUrlAuthorizationConfigurer<*>.ExpressionInterceptUrlRegistry?>(expressionInterceptUrlRegistry, Mockito.times(1))
                .anyRequest()
            Mockito.verify<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl?>(authorizedUrl, Mockito.times(1))
                .permitAll()
            Mockito.verify(httpSecurity, Mockito.times(1)).sessionManagement()
            Mockito.verify(sessionManagementConfigurer, Mockito.times(1))
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            Mockito.verify(httpSecurity, Mockito.times(1)).exceptionHandling()
            Mockito.verify(exceptionHandlingConfigurer, Mockito.times(1)).authenticationEntryPoint(
                ArgumentMatchers.any(
                    HttpStatusEntryPoint::class.java
                )
            )
            Mockito.verify(httpSecurity, Mockito.times(1)).addFilterBefore(
                ArgumentMatchers.same(preAuthorizationFilter), ArgumentMatchers.eq(
                    UsernamePasswordAuthenticationFilter::class.java
                )
            )
        }
    }
}