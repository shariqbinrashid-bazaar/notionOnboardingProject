package com.bazaar.api.template.security.config

import com.bazaar.api.template.security.filter.PreAuthorizationFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.lang.Exception


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ApplicationSecurityConfiguration(private val preAuthorizationFilter: PreAuthorizationFilter) :
    WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    public override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests().anyRequest().permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
            .and()
            .addFilterBefore(preAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}