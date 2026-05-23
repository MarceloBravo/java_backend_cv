package com.mabc.back_cv.web.controllers.auth;

import com.mabc.back_cv.web.dto.AuthTokens;
import com.mabc.back_cv.web.dto.RefreshTokenRequest;
import com.mabc.back_cv.web.entities.User;
import com.mabc.back_cv.web.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        return ResponseEntity.ok(toTokenResponse(authService.register(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        return ResponseEntity.ok(toTokenResponse(authService.authenticate(user)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(toTokenResponse(authService.refreshTokens(request.refreshToken())));
    }

    private Map<String, String> toTokenResponse(AuthTokens tokens) {
        return Map.of(
                "accessToken", tokens.accessToken(),
                "refreshToken", tokens.refreshToken()
        );
    }
}
