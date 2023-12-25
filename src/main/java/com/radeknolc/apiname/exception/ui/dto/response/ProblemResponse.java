package com.radeknolc.apiname.exception.ui.dto.response;

import com.radeknolc.apiname.exception.domain.enumeration.ProblemCode;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Clock;

public class ProblemResponse {

    private final long timestamp;
    private final ProblemCode problemCode;
    private final String requestUri;

    public ProblemResponse(Clock clock, ProblemCode problemCode, HttpServletRequest httpServletRequest) {
        this.timestamp = clock.millis();
        this.problemCode = problemCode;
        this.requestUri = httpServletRequest.getRequestURI();
    }

    @SuppressWarnings("unused") // Used in JSON object in response
    public long getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused") // Used in JSON object in response
    public ProblemCode getProblemCode() {
        return problemCode;
    }

    @SuppressWarnings("unused") // Used in JSON object in response
    public String getRequestUri() {
        return requestUri;
    }
}
