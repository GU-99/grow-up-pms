package com.growup.pms.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class MdcFilter extends OncePerRequestFilter {
    public static final String TRACKING_ID = "trackingId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            MDC.put(TRACKING_ID, UUID.randomUUID().toString());
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
