package cz.radeknolc.boilerplate.domain.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Clock;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {

    private final long timestamp;
    private final T message;
    private final Map<String, String> errors;
    private final String requestUri;

    public ErrorResponse(Clock clock, T message, HttpServletRequest httpServletRequest) {
        this(clock, message, Map.of(), httpServletRequest);
    }

    public ErrorResponse(Clock clock, T message, Map<String, String> errors, HttpServletRequest httpServletRequest) {
        timestamp = clock.millis();
        this.errors = errors;
        this.message = message;
        this.requestUri = httpServletRequest.getRequestURI();
    }
}