package com.radeknolc.apiname.exception.domain.enumeration;

public enum AuthenticationProblemCode implements ProblemCode {
    BAD_CREDENTIALS,
    ACCOUNT_INACTIVE,
    ACCOUNT_EXPIRED,
    ACCOUNT_LOCKED,
    CREDENTIALS_EXPIRED,
    UNAUTHORIZED,
    ACCESS_DENIED,
}
