package org.example.bookmycut.services.auth;

import org.example.bookmycut.dtos.auth.AuthResponseDto;
import org.example.bookmycut.dtos.auth.LoginUserDto;
import org.example.bookmycut.dtos.auth.RegisterUserDto;
import org.example.bookmycut.exceptions.DuplicateEntityException;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.repositories.AppUserRepository;
import org.example.bookmycut.security.CustomUserDetails;
import org.example.bookmycut.services.contracts.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(AppUserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto login(LoginUserDto dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword())
        );


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        assert userDetails != null;

        String token = jwtService.generateToken(userDetails.getUsername());
        return new AuthResponseDto(
                userDetails.getUsername(),
                token,
                userDetails.getRole(),
                jwtService.extractExpirationAsInstant(token)
        );
    }

    @Transactional
    @Override
    public AuthResponseDto register(RegisterUserDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new DuplicateEntityException("User", "email", registerDto.getEmail());
        }

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new DuplicateEntityException("User", "username", registerDto.getUsername());
        }


        AppUser user = new AppUser();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        AppUser saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getUsername());

        return new AuthResponseDto(
                saved.getUsername(),
                token,
                saved.getRole(),
                jwtService.extractExpirationAsInstant(token)
        );
    }
}
