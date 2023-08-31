package cz.radeknolc.appname.shared.problem.domain.enumeration;

import cz.radeknolc.appname.shared.problem.domain.ProblemCode;

public enum ApiProblemCode implements ProblemCode {
    VALIDATION_ERROR,
    ACCOUNT_ALREADY_EXISTS,
    DEFAULT_ROLE_NOT_EXISTS,
}
