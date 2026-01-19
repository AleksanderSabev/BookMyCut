package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.dtos.AppointmentDto;

import java.util.List;

public interface AppUserService {

    AppUserDto registerUser(AppUserDto appUserDto);

    AppUserDto updateUser(AppUserDto appUserDto);

    AppUserDto removeUser(Long userId);

    AppUserDto getUserById(Long userId);

    List<AppUserDto> getAllUsers();

    List<AppointmentDto> getUserAppointments(Long userId);
}
