package com.radeknolc.apiname.authentication.domain.usecase;

import com.radeknolc.apiname.authentication.ui.dto.request.SignInRequest;

public interface AuthenticationUseCase {

    String signIn(SignInRequest request);
}
