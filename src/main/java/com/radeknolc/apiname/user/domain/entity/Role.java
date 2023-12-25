package com.radeknolc.apiname.user.domain.entity;


import com.radeknolc.libs.ddd.domain.base.BaseBuilder;
import com.radeknolc.libs.ddd.domain.base.BaseEntity;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

public class Role extends BaseEntity implements GrantedAuthority {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private final UUID id;
    private final String name;

    protected Role(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return name.toUpperCase();
    }

    public static class Builder implements BaseBuilder<Role> {

        private UUID id;
        private String name;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Role build() {
            return new Role(id, name);
        }
    }
}
