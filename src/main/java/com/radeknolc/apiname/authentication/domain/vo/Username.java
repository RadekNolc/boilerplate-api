package com.radeknolc.apiname.authentication.domain.vo;

import com.radeknolc.libs.ddd.domain.base.ValueObject;
import com.radeknolc.libs.tools.StringTools;

public class Username extends ValueObject<String> {

    public Username(String value) {
        super(value);
    }

    @Override
    protected boolean isEnabledValidation() {
        return true;
    }

    @Override
    protected boolean isValid(String value) {
        return !StringTools.isBlank(value);
    }
}
