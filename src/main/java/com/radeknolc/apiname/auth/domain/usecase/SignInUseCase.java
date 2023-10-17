package com.radeknolc.apiname.auth.domain.usecase;

import com.radeknolc.apiname.auth.ui.dto.request.SignInRequest;
import com.radeknolc.apiname.auth.ui.dto.response.SignInResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface SignInUseCase {

    SignInResponse signIn(@Valid SignInRequest request);
}
