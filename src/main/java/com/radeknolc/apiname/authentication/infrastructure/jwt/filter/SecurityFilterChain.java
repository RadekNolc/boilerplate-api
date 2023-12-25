package com.radeknolc.apiname.authentication.infrastructure.jwt.filter;

import com.radeknolc.apiname.authentication.infrastructure.jwt.handler.AccessDeniedHandler;
import com.radeknolc.apiname.authentication.infrastructure.jwt.handler.AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component("securityFilterChainImpl")
public class SecurityFilterChain {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationTokenFilter authenticationTokenFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityFilterChain(AuthenticationProvider authenticationProvider, AuthenticationTokenFilter authenticationTokenFilter, AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults()); // Setting-up CORS
        httpSecurity.csrf(AbstractHttpConfigurer::disable); // Disabling CSRF - Not needed because of using JWT

        httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/api/authentication/signIn").anonymous()
                .requestMatchers("/api/user/create").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/**").denyAll()
        );

        httpSecurity.exceptionHandling(handler -> {
            handler.authenticationEntryPoint(authenticationEntryPoint);
            handler.accessDeniedHandler(accessDeniedHandler);
        });

        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
