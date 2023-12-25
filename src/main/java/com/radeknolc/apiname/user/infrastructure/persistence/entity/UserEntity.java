package com.radeknolc.apiname.user.infrastructure.persistence.entity;

import com.radeknolc.apiname.user.domain.enumeration.AccountStatus;
import com.radeknolc.apiname.user.domain.enumeration.ActivityStatus;
import com.radeknolc.apiname.user.domain.enumeration.CredentialsStatus;
import com.radeknolc.libs.ddd.domain.base.AuditedEntity;
import com.radeknolc.libs.ddd.domain.base.BaseBuilder;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "user")
public class UserEntity extends AuditedEntity {

    @Column(unique = true, nullable = false, length = 32)
    private String username;
    @Column(nullable = false, length = 320)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @Enumerated(EnumType.STRING)
    private CredentialsStatus credentialsStatus;
    @ManyToMany(targetEntity = RoleEntity.class, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role_map",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    protected UserEntity() {
        super();
    }

    protected UserEntity(UUID id, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, String username, String email, String password, ActivityStatus activityStatus, AccountStatus accountStatus, CredentialsStatus credentialsStatus, Set<RoleEntity> roles) {
        super(id, createdAt, createdBy, updatedAt, updatedBy);
        this.username = username;
        this.email = email;
        this.password = password;
        this.activityStatus = activityStatus;
        this.accountStatus = accountStatus;
        this.credentialsStatus = credentialsStatus;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

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

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public static class Builder implements BaseBuilder<UserEntity> {
        private UUID id;
        private String username;
        private String email;
        private String password;
        private ActivityStatus activityStatus = ActivityStatus.getDefault();
        private AccountStatus accountStatus = AccountStatus.getDefault();
        private CredentialsStatus credentialsStatus = CredentialsStatus.getDefault();
        private Set<RoleEntity> roles = Set.of();
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

        public Builder roles(Set<RoleEntity> roles) {
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

        public UserEntity build() {
            return new UserEntity(id, createdAt, createdBy, updatedAt, updatedBy, username, email, password, activityStatus, accountStatus, credentialsStatus, roles);
        }
    }
}
