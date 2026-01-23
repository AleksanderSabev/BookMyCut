package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.dtos.appointment.AppointmentResponseDto;
import org.example.bookmycut.dtos.auth.RegisterUserDto;

import java.util.List;

public interface AppUserService {

    AppUserDto registerUser(RegisterUserDto registerDto);

    void updateUser(AppUserDto appUserDto);

    void updatePassword(Long userId, String newPassword);

    void removeUser(Long userId);

    void makeAdmin(Long userId);

    AppUserDto getUserById(Long userId);

    List<AppUserDto> getAllUsers();

    List<AppointmentResponseDto> getUserAppointments(Long userId);
}
