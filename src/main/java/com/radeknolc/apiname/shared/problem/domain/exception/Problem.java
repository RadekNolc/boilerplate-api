package com.radeknolc.apiname.shared.problem.domain.exception;

import com.radeknolc.apiname.shared.problem.domain.ProblemCode;

public class Problem extends RuntimeException {

    public Problem(ProblemCode code) {
        super(code.toString());
    }
}
