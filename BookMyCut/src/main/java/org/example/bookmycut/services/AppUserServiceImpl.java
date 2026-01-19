package org.example.bookmycut.services;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.dtos.AppointmentDto;
import org.example.bookmycut.exceptions.DuplicateEntityException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.helpers.mappers.AppUserMapper;
import org.example.bookmycut.helpers.mappers.AppointmentMapper;
import org.example.bookmycut.models.AppUser;
import org.example.bookmycut.repositories.AppUserRepository;
import org.example.bookmycut.services.contracts.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public AppUserServiceImpl(AppUserRepository userRepository, AppUserMapper userMapper, AppointmentMapper appointmentMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.appointmentMapper = appointmentMapper;
    }

    @Transactional
    @Override
    public AppUserDto registerUser(AppUserDto appUserDto) {
        if(userRepository.existsByEmail(appUserDto.getEmail())){
            throw new DuplicateEntityException("User", "email", appUserDto.getEmail());
        }

        AppUser user = userMapper.toEntity(appUserDto);
        AppUser saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    @Override
    public AppUserDto updateUser(AppUserDto appUserDto) {
        AppUser userToUpdate = userRepository.findById(appUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User", appUserDto.getId()));

        if(!userToUpdate.getEmail().equals(appUserDto.getEmail()) &&
                userRepository.existsByEmail(appUserDto.getEmail())){
            throw new DuplicateEntityException("User", "email", appUserDto.getEmail());
        }

        userToUpdate.setUsername(appUserDto.getUsername());
        userToUpdate.setEmail(appUserDto.getEmail());

        AppUser saved = userRepository.save(userToUpdate);
        return userMapper.toDto(saved);
    }

    @Transactional
    @Override
    public AppUserDto removeUser(Long userId) {
        AppUser userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        userRepository.delete(userToDelete);
        return userMapper.toDto(userToDelete);
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
    public List<AppointmentDto> getUserAppointments(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        return user.getAppointments()
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }
}
