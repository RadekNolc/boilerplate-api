package com.radeknolc.apiname.authentication.infrastructure.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radeknolc.apiname.shared.problem.ui.dto.response.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;

import static com.radeknolc.apiname.shared.problem.domain.enumeration.ApiProblemCode.ACCESS_DENIED;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final Clock clock;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Attempt to access resource resulted in access denied: {}", request.getRequestURI());
        response.setContentType("application/json");
        response.setStatus(403);
        response.getWriter().write(objectMapper.writeValueAsString(new ProblemResponse(clock, ACCESS_DENIED, request)));
    }
}
