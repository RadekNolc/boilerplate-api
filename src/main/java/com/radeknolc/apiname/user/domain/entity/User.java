package com.radeknolc.apiname.user.domain.entity;

import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.libs.ddd.domain.base.AuditedEntity;
import com.radeknolc.libs.ddd.domain.base.BaseBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class User extends AuditedEntity implements UserDetails {

    private final String username;
    private final String email;
    private final String password;
    private final ActivityStatus activityStatus;
    private final AccountStatus accountStatus;
    private final CredentialsStatus credentialsStatus;
    private final Set<Role> roles;

    protected User(UUID id, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, String username, String email, String password, ActivityStatus activityStatus, AccountStatus accountStatus, CredentialsStatus credentialsStatus, Set<Role> roles) {
        super(id, createdAt, createdBy, updatedAt, updatedBy);
        this.username = username;
        this.email = email;
        this.password = password;
        this.activityStatus = activityStatus;
        this.accountStatus = accountStatus;
        this.credentialsStatus = credentialsStatus;
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

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public CredentialsStatus getCredentialsStatus() {
        return credentialsStatus;
    }

    public Set<Role> getRoles() {
        return roles;
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

    public static class Builder implements BaseBuilder<User> {
        private UUID id;
        private String username;
        private String email;
        private String password;
        private ActivityStatus activityStatus = ActivityStatus.getDefault();
        private AccountStatus accountStatus = AccountStatus.getDefault();
        private CredentialsStatus credentialsStatus = CredentialsStatus.getDefault();
        private Set<Role> roles = Set.of();
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private String updatedBy;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder activityStatus(ActivityStatus activityStatus) {
            this.activityStatus = activityStatus;
            return this;
        }

        public Builder accountStatus(AccountStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        public Builder credentialsStatus(CredentialsStatus credentialsStatus) {
            this.credentialsStatus = credentialsStatus;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public User build() {
            return new User(id, createdAt, createdBy, updatedAt, updatedBy, username, email, password, activityStatus, accountStatus, credentialsStatus, roles);
        }
    }
}
