package cz.radeknolc.boilerplate.infrastructure.problem;

public class Problem extends RuntimeException {

    public Problem(ProblemCode code) {
        super(code.toString());
    }
}
