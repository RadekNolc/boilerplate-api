package cz.radeknolc.appname.shared.problem.ui.dto.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Clock;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ProblemResponse<T> {

    private final long timestamp;
    private final T message;
    private final Map<String, String> errors;
    private final String requestUri;

    public ProblemResponse(Clock clock, T message, HttpServletRequest httpServletRequest) {
        this(clock, message, Map.of(), httpServletRequest);
    }

    public ProblemResponse(Clock clock, T message, Map<String, String> errors, HttpServletRequest httpServletRequest) {
        this(clock.millis(), message, errors, httpServletRequest.getRequestURI());
    }
}
