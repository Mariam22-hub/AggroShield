package com.example.util;

public class JwtAuthConstants {

    public static final String USERID = "X-User-Id";
    public static final String ROLE_FIELD = "X-User-Role";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String UNAUTHORIZED_ACCESS = "unauthorized access";
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";

    private JwtAuthConstants() {
        // Prevent instantiation
    }
}