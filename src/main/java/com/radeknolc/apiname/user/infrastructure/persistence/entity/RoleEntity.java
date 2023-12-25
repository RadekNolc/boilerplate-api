package com.radeknolc.apiname.user.infrastructure.persistence.entity;

import com.radeknolc.libs.ddd.domain.base.BaseBuilder;
import com.radeknolc.libs.ddd.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity(name = "role")
public class RoleEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    protected RoleEntity() {}

    protected RoleEntity(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Builder implements BaseBuilder<RoleEntity> {

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
        public RoleEntity build() {
            return new RoleEntity(id, name);
        }
    }
}
