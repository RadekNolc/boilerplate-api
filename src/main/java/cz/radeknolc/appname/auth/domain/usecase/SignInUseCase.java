package cz.radeknolc.appname.auth.domain.usecase;

import cz.radeknolc.appname.auth.ui.dto.request.SignInRequest;
import cz.radeknolc.appname.auth.ui.dto.response.SignInResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

@Validated
public interface SignInUseCase {

    SignInResponse signIn(@Valid SignInRequest request);
}
