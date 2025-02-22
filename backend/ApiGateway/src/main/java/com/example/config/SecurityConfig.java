package com.example.config;

import com.example.filter.JwtAuthenticationFilter;
import com.example.service.UserSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserSessionService userSessionService;

    private final String secretKey;

    public SecurityConfig(UserSessionService userSessionService,
                          @Value("${aggressive.secret.key}") String secretKey) {
        this.userSessionService = userSessionService;
        this.secretKey = secretKey;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  ///stop csrf to use jwt
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("*/auth/**").permitAll()
                        .pathMatchers("/users/me").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/users/**").hasRole("ADMIN")
                        .pathMatchers("/analyze_behavior/**").permitAll()
                        .pathMatchers("/alerts/admin/users").hasRole("ADMIN")
                        .pathMatchers("/alerts/**").hasAnyRole("USER","ADMIN")
                        .pathMatchers("/predictions/**").hasAnyRole("USER","ADMIN")
                        .pathMatchers("/reports/**").hasAnyRole("USER","ADMIN")
                        .anyExchange().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(userSessionService, secretKey), SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}