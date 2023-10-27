package com.radeknolc.apiname.general.entity;

import java.time.LocalDateTime;

public interface AuditedEntity extends BaseEntity {
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getUpdatedAt();
    String getUpdatedBy();
}
