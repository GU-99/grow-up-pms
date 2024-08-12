package com.growup.pms.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String resolvedToken = resolveToken(request);

        if (StringUtils.hasText(resolvedToken) && tokenProvider.validateToken(resolvedToken)) {
            Authentication authentication = tokenProvider.getAuthentication(resolvedToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authenticated user: {}, uri: {}", authentication.getName(), request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isValidHeader(bearerToken)) {
            return bearerToken.substring(JwtConstants.BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean isValidHeader(String header) {
        return header != null && header.startsWith(JwtConstants.BEARER_PREFIX);
    }
}
