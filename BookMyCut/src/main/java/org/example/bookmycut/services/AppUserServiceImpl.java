package org.example.bookmycut.services;

import org.example.bookmycut.dtos.*;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.dtos.auth.UpdatePasswordDto;
import org.example.bookmycut.enums.Role;
import org.example.bookmycut.exceptions.AlreadyHasThisRoleException;
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

    private final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match!";
    private final String PASSWORD_IS_THE_SAME = "Password is the same as the old one.";

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
    public void updateUser(AppUserDto appUserDto) {
        AppUser userToUpdate = userRepository.findById(appUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User", appUserDto.getId()));

        if(!userToUpdate.getEmail().equals(appUserDto.getEmail()) &&
                userRepository.existsByEmail(appUserDto.getEmail())){
            throw new DuplicateEntityException("User", "email", appUserDto.getEmail());
        }

        userToUpdate.setUsername(appUserDto.getUsername());
        userToUpdate.setEmail(appUserDto.getEmail());

        userRepository.save(userToUpdate);
    }

    @Transactional
    @Override
    public void updatePassword(Long userId, UpdatePasswordDto passwordDto) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        if(!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())){
            throw new IllegalArgumentException(PASSWORDS_DO_NOT_MATCH);
        }
        if(passwordEncoder.matches(passwordDto.getConfirmPassword(), user.getPassword())){
            throw new IllegalArgumentException(PASSWORD_IS_THE_SAME);
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getConfirmPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Long userId) {
        AppUser userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        userRepository.delete(userToDelete);
    }

    @Transactional
    @Override
    public void makeAdmin(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        if(user.getRole().equals(Role.ADMIN)){
            throw new AlreadyHasThisRoleException(Role.ADMIN);
        }
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void makeClient(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        if(user.getRole().equals(Role.CLIENT)){
            throw new AlreadyHasThisRoleException(Role.CLIENT);
        }
        user.setRole(Role.CLIENT);
        userRepository.save(user);
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
