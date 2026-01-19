package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.services.contracts.AppUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {//TODO match method names with the services

    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public AppUserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public AppUserDto updateUser(@PathVariable Long id, @RequestBody AppUserDto dto) {
        dto.setId(id);
        return userService.updateUser(dto);
    }

    @PutMapping("/{id}/password")
    public void updatePassword(@PathVariable Long id) {
    }

    @DeleteMapping("/{id}")
    public AppUserDto deleteUser(@PathVariable Long id) {
        return userService.removeUser(id);
    }

    @PostMapping("/{id}/promote")
    public void makeToAdmin(@PathVariable Long id) {
        userService.makeAdmin(id);
    }

    @GetMapping
    public List<AppUserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}

