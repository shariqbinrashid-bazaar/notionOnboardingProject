package com.bazaar.api.template.security.filter

import com.bazaar.api.common.constant.BazaarConstant.IS_VALIDATED_ATTRIBUTE
import com.bazaar.api.common.constant.BazaarConstant.USER_ID_ATTRIBUTE
import com.bazaar.api.common.constant.BazaarConstant.USER_SCOPES_ATTRIBUTE
import org.apache.commons.lang3.StringUtils.defaultString
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.lang.Boolean.parseBoolean
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class PreAuthorizationFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // If request was validated, inject SecurityPrincipal to Current Thread's SecurityContext for Authorization
            val isValidated: String = defaultString(httpServletRequest.getHeader(IS_VALIDATED_ATTRIBUTE))
            if (parseBoolean(isValidated)) {
                val scopes = httpServletRequest.getHeader(USER_SCOPES_ATTRIBUTE).split(",".toRegex())
                    .toTypedArray()
                injectSecurityPrincipal(httpServletRequest.getHeader(USER_ID_ATTRIBUTE), scopes)
            }

            // Resume filter chain
            filterChain.doFilter(httpServletRequest, httpServletResponse)
        } catch (e: Exception) {
            httpServletResponse.sendError(UNAUTHORIZED.value(), "Invalid token")
        }
    }

    private fun injectSecurityPrincipal(userName: String, scopes: Array<String>) {
        // add authenticated principal to current thread's security pool
        if (SecurityContextHolder.getContext().authentication == null) {
            val authorities: List<GrantedAuthority> = Stream.of(*scopes).map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }.collect(Collectors.toList())
            val userDetails: UserDetails = User(userName, "", authorities)
            val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }
}