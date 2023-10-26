package com.radeknolc.apiname.shared.problem.domain.exception;

import com.radeknolc.apiname.shared.problem.domain.ProblemCode;
import lombok.Getter;

@Getter
public class Problem extends RuntimeException {

    private final ProblemCode problemCode;

    public Problem(ProblemCode code) {
        super(code.toString());
        this.problemCode = code;
    }
}
