package com.radeknolc.apiname.user.ui;

import com.radeknolc.apiname.user.ui.dto.request.CreateUserRequest;
import com.radeknolc.apiname.user.domain.usecase.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping("/create")
    public void create(@RequestBody CreateUserRequest createUserRequest) {
        log.debug("Create user: {}", createUserRequest);
        createUserUseCase.createNewUser(createUserRequest);
    }
}
