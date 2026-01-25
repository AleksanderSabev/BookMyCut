package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.dtos.auth.UpdatePasswordDto;
import org.example.bookmycut.helpers.security.SecurityUtils;
import org.example.bookmycut.services.contracts.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private  final String USER_UPDATED_SUCCESSFULLY = "User info updated successfully!.";
    private  final String USER_REMOVED_SUCCESSFULLY = "User removed successfully!.";
    private  final String PASSWORD_UPDATED_SUCCESSFULLY = "Your password has been updated successfully.";
    private  final String USER_PROMOTED_SUCCESSFULLY = "User with ID %d is now ADMIN.";
    private  final String USER_DEMOTED_SUCCESSFULLY = "User with ID %d is now CLIENT.";

    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AppUserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public AppUserDto getLoggedUser(){
        Long userId = SecurityUtils.getCurrentUserId();
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> updateUserAsAdmin(@PathVariable Long id, @Valid @RequestBody AppUserDto dto) {
        dto.setId(id);
        userService.updateUser(dto);
        return ResponseEntity.ok(USER_UPDATED_SUCCESSFULLY);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<@NonNull String> updateUserAsClient(@Valid @RequestBody AppUserDto dto){
        Long userId = SecurityUtils.getCurrentUserId();
        dto.setId(userId);
        userService.updateUser(dto);
        return ResponseEntity.ok(USER_UPDATED_SUCCESSFULLY);
    }

    @PutMapping("/me/password")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<@NonNull String> updatePassword(@Valid @RequestBody UpdatePasswordDto passwordDto) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updatePassword(userId, passwordDto);
        return ResponseEntity.ok(PASSWORD_UPDATED_SUCCESSFULLY);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok(USER_REMOVED_SUCCESSFULLY);
    }

    @PostMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> makeAdmin(@PathVariable Long id) {
        userService.makeAdmin(id);
        return ResponseEntity.ok(String.format(USER_PROMOTED_SUCCESSFULLY, id));
    }

    @PostMapping("/{id}/demote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> makeUser(@PathVariable Long id) {
        userService.makeClient(id);
        return ResponseEntity.ok(String.format(USER_DEMOTED_SUCCESSFULLY, id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}

