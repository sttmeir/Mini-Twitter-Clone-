package com.mfortune.nottify.constant;

public class SecurityConstant {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_DENIED_MESSAGE = "You are not allowed to access this page";
    public static final String FORBIDDEN_MESSAGE = "Login is needed to access this page";
    public static final String[] PUBLIC_URLS = {"/api/auth/login", "api/auth/signup",
            "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**", "/swagger/", "/swagger-ui/"};
}
