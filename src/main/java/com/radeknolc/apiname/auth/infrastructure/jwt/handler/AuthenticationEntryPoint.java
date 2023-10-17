package com.radeknolc.apiname.auth.infrastructure.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radeknolc.apiname.shared.problem.ui.dto.response.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;

import static com.radeknolc.apiname.shared.problem.domain.enumeration.ApiProblemCode.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final Clock clock;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("Attempt to access resource as unauthorized: {}", request.getRequestURI());
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(objectMapper.writeValueAsString(new ProblemResponse<>(clock, UNAUTHORIZED, request)));
    }
}
