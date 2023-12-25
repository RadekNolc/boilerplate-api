package com.radeknolc.apiname.user.ui;

import com.radeknolc.apiname.user.domain.entity.User;
import com.radeknolc.apiname.user.domain.usecase.UserUseCase;
import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import com.radeknolc.apiname.user.ui.dto.response.CreateUserResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserUseCase userUseCase;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        logger.debug("Create user: {}", createUserRequest);
        User createdUser = userUseCase.createUser(createUserRequest);
        return ResponseEntity.ok(new CreateUserResponse(createdUser));
    }
}
