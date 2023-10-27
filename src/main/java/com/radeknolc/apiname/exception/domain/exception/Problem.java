package com.radeknolc.apiname.exception.domain.exception;

import com.radeknolc.apiname.exception.domain.enumeration.ProblemCode;
import lombok.Getter;

@Getter
public class Problem extends RuntimeException {

    private final ProblemCode problemCode;

    public Problem(ProblemCode code) {
        super(code.toString());
        this.problemCode = code;
    }
}
