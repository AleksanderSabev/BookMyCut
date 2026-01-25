package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.auth.AuthResponseDto;
import org.example.bookmycut.dtos.auth.LoginUserDto;
import org.example.bookmycut.dtos.auth.RegisterUserDto;

public interface AuthService {

    AuthResponseDto login(LoginUserDto dto);

    AuthResponseDto register(RegisterUserDto dto);
}
