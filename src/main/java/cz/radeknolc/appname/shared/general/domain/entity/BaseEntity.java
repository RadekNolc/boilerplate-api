package cz.radeknolc.appname.shared.general.domain.entity;

import java.util.UUID;

public interface BaseEntity {
    UUID getId();
    void setId(UUID uuid);
}
