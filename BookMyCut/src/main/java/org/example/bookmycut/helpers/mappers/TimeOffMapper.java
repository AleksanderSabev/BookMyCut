package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.dtos.TimeOffDto;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.EmployeeTimeOff;
import org.springframework.stereotype.Component;

@Component
public class TimeOffMapper {

    public EmployeeTimeOff toEntity(TimeOffDto dto, Employee employee) {
        if (dto == null || employee == null) {
            return null;
        }

        EmployeeTimeOff entity = new EmployeeTimeOff();
        entity.setEmployee(employee);
        entity.setStartDateTime(dto.getStartDateTime());
        entity.setEndDateTime(dto.getEndDateTime());
        entity.setReason(dto.getReason());

        return entity;
    }

    public TimeOffDto toDto(EmployeeTimeOff entity) {
        if (entity == null) {
            return null;
        }

        TimeOffDto dto = new TimeOffDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setStartDateTime(entity.getStartDateTime());
        dto.setEndDateTime(entity.getEndDateTime());
        dto.setReason(entity.getReason());

        return dto;
    }
}
