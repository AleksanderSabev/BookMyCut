package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.dtos.auth.UpdatePasswordDto;

import java.util.List;

public interface AppUserService {

    void updateUser(AppUserDto appUserDto);

    void updatePassword(Long userId, UpdatePasswordDto passwordDto);

    void removeUser(Long userId);

    void makeAdmin(Long userId);

    void makeClient(Long userId);

    AppUserDto getUserById(Long userId);

    List<AppUserDto> getAllUsers();
}
