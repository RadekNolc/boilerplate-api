package cz.radeknolc.appname.user.domain.entity;

import cz.radeknolc.appname.user.domain.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private Status status;
    private Set<Role> roles = Set.of();

    public User(String username, String email, String password, Status status) {
        this(null, username, email, password, status, Set.of());
    }

    public User(String username, String email, String password, Status status, Set<Role> roles) {
        this(username, email, password, status);
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != Status.ACCOUNT_EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != Status.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status != Status.CREDENTIALS_EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }
}
