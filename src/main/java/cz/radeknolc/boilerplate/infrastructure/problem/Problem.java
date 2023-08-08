package cz.radeknolc.boilerplate.infrastructure.problem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Problem extends RuntimeException {

    public Problem(ProblemCode code) {
        super(code.toString());
    }
}
