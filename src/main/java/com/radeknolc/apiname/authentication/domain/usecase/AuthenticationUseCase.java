package com.radeknolc.apiname.authentication.domain.usecase;

import com.radeknolc.apiname.authentication.domain.vo.Password;
import com.radeknolc.apiname.authentication.domain.vo.Username;
import org.springframework.security.core.Authentication;

public interface AuthenticationUseCase {

    Authentication createAuthentication(Username username, Password password);
}
