package com.radeknolc.apiname.shared.problem.domain.enumeration;

import com.radeknolc.apiname.shared.problem.domain.ProblemCode;

public enum ApiProblemCode implements ProblemCode {
    BAD_CREDENTIALS,
    ACCOUNT_INACTIVE,
    ACCOUNT_EXPIRED,
    ACCOUNT_LOCKED,
    CREDENTIALS_EXPIRED,
    UNAUTHORIZED,
    ACCESS_DENIED,
    VALIDATION_FAILED,
    ACCOUNT_ALREADY_EXISTS,
    DEFAULT_ROLE_NOT_EXISTS,
    UNKNOWN,
}
