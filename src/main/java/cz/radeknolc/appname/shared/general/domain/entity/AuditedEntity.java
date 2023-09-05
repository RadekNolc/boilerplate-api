package cz.radeknolc.appname.shared.general.domain.entity;

import java.time.LocalDateTime;

public interface AuditedEntity extends BaseEntity {
    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);

    String getCreatedBy();
    void setCreatedBy(String createdBy);

    LocalDateTime getUpdatedAt();
    void setUpdatedAt(LocalDateTime createdAt);

    String getUpdatedBy();
    void setUpdatedBy(String createdBy);
}
