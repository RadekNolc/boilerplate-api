package cz.radeknolc.boilerplate.adapter.out.persistence.user;

import cz.radeknolc.boilerplate.adapter.out.persistence.role.RoleEntity;
import cz.radeknolc.boilerplate.domain.user.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToMany(targetEntity = RoleEntity.class, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role_map",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;
}
