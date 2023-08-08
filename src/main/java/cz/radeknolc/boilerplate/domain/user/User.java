package cz.radeknolc.boilerplate.domain.user;

import cz.radeknolc.boilerplate.adapter.out.persistence.user.UserEntity;
import cz.radeknolc.boilerplate.infrastructure.mapping.MappableModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements MappableModel<UserEntity> {

    private UUID id;
    private String displayName;
    private String email;
    private String password;
    private Status status;
    private Set<Role> roles;

    public User(String displayName, String email, String password, Status status) {
        this(null, displayName, email, password, status, Set.of());
    }

    @Override
    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setDisplayName(displayName);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setStatus(status);
        userEntity.setRoles(roles.stream().map(Role::toEntity).collect(Collectors.toSet()));
        return userEntity;
    }
}
