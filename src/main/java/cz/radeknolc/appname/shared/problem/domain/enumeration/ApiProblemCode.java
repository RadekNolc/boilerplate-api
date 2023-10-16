package cz.radeknolc.appname.shared.problem.domain.enumeration;

import cz.radeknolc.appname.shared.problem.domain.ProblemCode;

public enum ApiProblemCode implements ProblemCode {
    BAD_CREDENTIALS,
    ACCOUNT_INACTIVE,
    ACCOUNT_EXPIRED,
    ACCOUNT_LOCKED,
    CREDENTIALS_EXPIRED,
    UNAUTHORIZED,
    ACCESS_DENIED,
    VALIDATION_ERROR,
    ACCOUNT_ALREADY_EXISTS,
    DEFAULT_ROLE_NOT_EXISTS,
}
