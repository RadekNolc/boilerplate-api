package cz.radeknolc.boilerplate.adapter.in.web.user;

import cz.radeknolc.boilerplate.application.usecase.user.RegisterUserUseCase;
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
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public void register(@RequestBody RegisterUserUseCase.Request registerUserDto) {
        log.debug("Register user: {}", registerUserDto);
        registerUserUseCase.registerNewUser(registerUserDto);
    }
}
