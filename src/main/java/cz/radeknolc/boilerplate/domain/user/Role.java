package cz.radeknolc.boilerplate.domain.user;


import cz.radeknolc.boilerplate.adapter.out.persistence.role.RoleEntity;
import cz.radeknolc.boilerplate.infrastructure.mapping.MappableModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority, MappableModel<RoleEntity> {

    private UUID id;
    private String name;
    private Set<User> users;

    public Role(String name) {
        this.name = name;
        users = Set.of();
    }

    @Override
    public String getAuthority() {
        return name.toUpperCase();
    }

    @Override
    public RoleEntity toEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setName(name);
        roleEntity.setUsers(users.stream().map(User::toEntity).collect(Collectors.toSet()));
        return roleEntity;
    }
}
