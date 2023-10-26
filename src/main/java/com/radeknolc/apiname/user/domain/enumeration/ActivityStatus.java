package com.radeknolc.apiname.user.domain.enumeration;

public enum ActivityStatus {
    INACTIVE,
    ACTIVE;

    public static ActivityStatus getDefault() {
        return INACTIVE;
    }
}
