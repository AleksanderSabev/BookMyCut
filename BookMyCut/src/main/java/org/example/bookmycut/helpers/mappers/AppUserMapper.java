package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.dtos.AppUserDto;
import org.example.bookmycut.models.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUser toEntity(AppUserDto dto) {
        if(dto == null) return null;

        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return user;
    }

    public AppUserDto toDto(AppUser entity) {
        if(entity == null) return null;

        AppUserDto dto = new AppUserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());


        return dto;
    }
}
