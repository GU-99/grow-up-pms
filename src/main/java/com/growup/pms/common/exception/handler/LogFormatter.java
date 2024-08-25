package com.growup.pms.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.event.Level;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogFormatter {
    private static final String LOG_MESSAGE_FORMAT = "[{}] ({} {}) {}";
    private static final String MDC_REQUEST_URI = "requestUri";
    private static final String MDC_REQUEST_METHOD = "method";

    public static void info(Exception ex, HttpServletRequest request) {
        logWithMdc(request, () ->
                log.info(LOG_MESSAGE_FORMAT, Level.INFO, request.getMethod(), request.getRequestURI(), ex.getMessage())
        );
    }

    public static void error(Exception ex, HttpServletRequest request) {
        logWithMdc(request, () ->
                log.error(LOG_MESSAGE_FORMAT, Level.ERROR, request.getMethod(), request.getRequestURI(), ex.getMessage(), ex)
        );
    }

    private static void logWithMdc(HttpServletRequest request, Runnable method) {
        try (MdcContext context = new MdcContext()) {
            context.put(MDC_REQUEST_URI, request.getRequestURI());
            context.put(MDC_REQUEST_METHOD, request.getMethod());

            method.run();
        }
    }

    private static final class MdcContext implements AutoCloseable {
        private final List<String> context = new ArrayList<>();

        public void put(String key, String value) {
            MDC.put(key, value);
            context.add(key);
        }

        @Override
        public void close() {
            context.forEach(MDC::remove);
            context.clear();
        }
    }
}
