package com.radeknolc.apiname.user.domain.enumeration;

public enum CredentialsStatus {
    OK,
    EXPIRED;

    public static CredentialsStatus getDefault() {
        return OK;
    }
}
