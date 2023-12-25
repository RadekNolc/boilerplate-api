package com.radeknolc.apiname.authentication.infrastructure.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radeknolc.apiname.exception.ui.dto.response.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;

import static com.radeknolc.apiname.exception.domain.enumeration.AuthenticationProblemCode.UNAUTHORIZED;


@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final Clock clock;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    public AuthenticationEntryPoint(Clock clock, ObjectMapper objectMapper) {
        this.clock = clock;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.warn("Attempt to access resource as unauthorized: {}", request.getRequestURI());
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(objectMapper.writeValueAsString(new ProblemResponse(clock, UNAUTHORIZED, request)));
    }
}
