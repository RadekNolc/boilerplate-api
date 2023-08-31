package cz.radeknolc.appname.shared.problem.domain.exception;

import cz.radeknolc.appname.shared.problem.domain.ProblemCode;

public class Problem extends RuntimeException {

    public Problem(ProblemCode code) {
        super(code.toString());
    }
}
