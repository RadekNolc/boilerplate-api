package com.radeknolc.apiname.authentication.infrastructure.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radeknolc.apiname.exception.ui.dto.response.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;

import static com.radeknolc.apiname.exception.domain.enumeration.AuthenticationProblemCode.ACCESS_DENIED;


@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final Clock clock;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(AccessDeniedHandler.class);

    public AccessDeniedHandler(Clock clock, ObjectMapper objectMapper) {
        this.clock = clock;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        logger.warn("Attempt to access resource resulted in access denied: {}", request.getRequestURI());
        response.setContentType("application/json");
        response.setStatus(403);
        response.getWriter().write(objectMapper.writeValueAsString(new ProblemResponse(clock, ACCESS_DENIED, request)));
    }
}
