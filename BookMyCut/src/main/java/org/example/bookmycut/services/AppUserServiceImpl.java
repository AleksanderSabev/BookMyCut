package org.example.bookmycut.services;

import org.example.bookmycut.dtos.*;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.dtos.auth.RegisterUserDto;
import org.example.bookmycut.enums.Role;
import org.example.bookmycut.exceptions.DuplicateEntityException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.helpers.mappers.AppUserMapper;
import org.example.bookmycut.helpers.mappers.AppointmentMapper;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.repositories.AppUserRepository;
import org.example.bookmycut.services.contracts.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;
    private final AppointmentMapper appointmentMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserServiceImpl(AppUserRepository userRepository,
                              AppUserMapper userMapper,
                              AppointmentMapper appointmentMapper,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.appointmentMapper = appointmentMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public AppUserDto registerUser(RegisterUserDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new DuplicateEntityException("User", "email", registerDto.getEmail());
        }

        AppUser user = new AppUser();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        AppUser saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }


    @Transactional
    @Override
    public void updateUser(AppUserDto appUserDto) {
        AppUser userToUpdate = userRepository.findById(appUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User", appUserDto.getId()));

        if(!userToUpdate.getEmail().equals(appUserDto.getEmail()) &&
                userRepository.existsByEmail(appUserDto.getEmail())){
            throw new DuplicateEntityException("User", "email", appUserDto.getEmail());
        }

        userToUpdate.setUsername(appUserDto.getUsername());
        userToUpdate.setEmail(appUserDto.getEmail());

        AppUser saved = userRepository.save(userToUpdate);
    }

    @Transactional
    @Override
    public void updatePassword(Long userId, String newPassword) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    @Override
    public void removeUser(Long userId) {
        AppUser userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        userRepository.delete(userToDelete);
    }

    @Override
    public void makeAdmin(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        user.setRole(Role.ADMIN);
        AppUser saved = userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public AppUserDto getUserById(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentResponseDto> getUserAppointments(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        return user.getAppointments()
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }
}
