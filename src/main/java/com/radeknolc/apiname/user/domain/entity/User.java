package com.radeknolc.apiname.user.domain.entity;

import com.radeknolc.apiname.shared.general.domain.entity.AuditedEntity;
import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements AuditedEntity, UserDetails {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private ActivityStatus activityStatus;
    private AccountStatus accountStatus;
    private CredentialsStatus credentialsStatus;
    @Builder.Default
    private Set<Role> roles = Set.of();
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

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
        return accountStatus != AccountStatus.EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus != AccountStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsStatus != CredentialsStatus.EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return activityStatus == ActivityStatus.ACTIVE;
    }
}
