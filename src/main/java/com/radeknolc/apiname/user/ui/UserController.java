package com.radeknolc.apiname.user.ui;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.usecase.UserUseCase;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import com.radeknolc.apiname.user.ui.dto.response.CreateUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.debug("Create user: {}", createUserRequest);
        User createdUser = userUseCase.createUser(createUserRequest);
        return ResponseEntity.ok(new CreateUserResponse(createdUser));
    }
}
