package com.radeknolc.apiname.user.ui.dto.response;

import com.radeknolc.apiname.user.domain.entity.User;

import java.util.UUID;

public record CreateUserResponse(UUID id, String username) {

    public CreateUserResponse(User user) {
        this(user.getId(), user.getUsername());
    }
}
