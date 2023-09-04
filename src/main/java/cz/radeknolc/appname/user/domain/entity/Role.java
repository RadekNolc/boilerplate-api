package cz.radeknolc.appname.user.domain.entity;


import cz.radeknolc.appname.shared.general.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
