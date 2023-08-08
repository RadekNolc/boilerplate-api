package cz.radeknolc.boilerplate.adapter.out.persistence.role;

import cz.radeknolc.boilerplate.adapter.out.persistence.user.UserEntity;
import cz.radeknolc.boilerplate.domain.user.Role;
import cz.radeknolc.boilerplate.infrastructure.mapping.MappableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity implements MappableEntity<Role> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    @Override
    public Role toModel() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setUsers(users.stream().map(UserEntity::toModel).collect(Collectors.toSet()));
        return role;
    }
}
