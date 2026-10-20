package com.aryan.ecommerce_backend.security.oauth;

import com.aryan.ecommerce_backend.auth.dto.AuthResponse;
import com.aryan.ecommerce_backend.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final JsonMapper jsonMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                        Authentication auth) throws IOException {
        OAuth2User u = (OAuth2User) auth.getPrincipal();
        AuthResponse tokens = authService.loginWithOAuth(
                u.getAttribute("email"), u.getAttribute("name"));
        res.setContentType("application/json");
        res.getWriter().write(jsonMapper.writeValueAsString(tokens));
    }
}
