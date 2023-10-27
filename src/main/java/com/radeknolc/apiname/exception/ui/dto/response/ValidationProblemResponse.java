package com.radeknolc.apiname.exception.ui.dto.response;

import com.radeknolc.apiname.exception.domain.entity.FieldProblem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.time.Clock;
import java.util.Map;

import static com.radeknolc.apiname.exception.domain.enumeration.GeneralProblemCode.VALIDATION_FAILED;

@Getter
public class ValidationProblemResponse extends ProblemResponse {

    private final Map<String, FieldProblem> fieldProblems;

    public ValidationProblemResponse(Clock clock, HttpServletRequest httpServletRequest, Map<String, FieldProblem> fieldProblems) {
        super(clock, VALIDATION_FAILED, httpServletRequest);
        this.fieldProblems = fieldProblems;
    }
}
