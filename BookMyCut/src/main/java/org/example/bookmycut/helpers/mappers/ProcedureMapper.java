package org.example.bookmycut.helpers.mappers;

import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.models.Procedure;
import org.springframework.stereotype.Component;

@Component
public class ProcedureMapper {

    public Procedure toEntity(ProcedureDto dto) {
        if (dto == null) return null;
        Procedure procedure = new Procedure(
                dto.getName(),
                dto.getDurationInMinutes(),
                dto.getPrice()
        );

        if (dto.getId() != null) {
            procedure.setId(dto.getId());
        }
        return procedure;
    }

    public ProcedureDto toDto(Procedure procedure) {
        if (procedure == null) return null;
        return new ProcedureDto(
                procedure.getId(),
                procedure.getName(),
                procedure.getDurationMinutes(),
                procedure.getPrice()
        );
    }
}
