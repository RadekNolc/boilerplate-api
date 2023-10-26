package com.radeknolc.apiname.shared.problem.ui.dto.response;

import com.radeknolc.apiname.shared.problem.domain.ProblemCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Clock;

@Getter
@AllArgsConstructor
public class ProblemResponse {

    private final long timestamp;
    private final ProblemCode problemCode;
    private final String requestUri;

    public ProblemResponse(Clock clock, ProblemCode problemCode, HttpServletRequest httpServletRequest) {
        this.timestamp = clock.millis();
        this.problemCode = problemCode;
        this.requestUri = httpServletRequest.getRequestURI();
    }
}
