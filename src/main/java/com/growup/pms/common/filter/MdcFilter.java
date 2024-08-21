package com.growup.pms.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class MdcFilter extends OncePerRequestFilter {
    public static final String TRACKING_ID = "traceId";
    public static final int LENGTH_TRACKING_ID = 11;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            MDC.put(TRACKING_ID, RandomStringUtils.randomAlphanumeric(LENGTH_TRACKING_ID).toUpperCase());
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
