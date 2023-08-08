package cz.radeknolc.boilerplate.application.usecase.user;

public interface RegisterUserUseCase {

    void registerNewUser(Request registerUserDto);

    record Request(String displayName, String email, String password) {
    }
}
