package com.radeknolc.apiname.user.domain.entity;


import com.radeknolc.apiname.shared.general.domain.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Data
@Builder
public class Role implements BaseEntity, GrantedAuthority {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private UUID id;
    private String name;

    @Override
    public String getAuthority() {
        return name.toUpperCase();
    }
}
