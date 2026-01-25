package org.example.bookmycut.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookmycut.enums.Role;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String username;

    private String jwtToken;

    private Role role;

    private Instant expiresAt;

}
