package com.radeknolc.apiname.user.domain.enumeration;

public enum AccountStatus {
    OK,
    LOCKED,
    EXPIRED;

    public static AccountStatus getDefault() {
        return OK;
    }
}
