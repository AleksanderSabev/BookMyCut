package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.auth.AuthResponseDto;
import org.example.bookmycut.dtos.auth.LoginUserDto;
import org.example.bookmycut.dtos.auth.RegisterUserDto;
import org.example.bookmycut.services.contracts.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final String LOGOUT_SUCCESSFUL= "Logged out successfully!";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<@NonNull AuthResponseDto> register(@Valid @RequestBody RegisterUserDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull AuthResponseDto> login(@Valid @RequestBody LoginUserDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // Stateless logout – client must delete JWT
    @PostMapping("/logout")
    public ResponseEntity<@NonNull String> logout(){
        return ResponseEntity.ok(LOGOUT_SUCCESSFUL);
    }
}

