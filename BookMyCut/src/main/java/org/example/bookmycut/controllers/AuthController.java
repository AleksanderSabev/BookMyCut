package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.dtos.auth.LoginUserDto;
import org.example.bookmycut.dtos.auth.RegisterUserDto;
import org.example.bookmycut.services.auth.JwtService;
import org.example.bookmycut.services.contracts.AppUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService userService;
    private final JwtService jwtService;

    public AuthController(AppUserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AppUserDto register(@RequestBody RegisterUserDto dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUserDto dto) {
        return null; // returns JWT token
    }
}

