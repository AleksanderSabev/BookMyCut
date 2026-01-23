package org.example.bookmycut.services.contracts;

import org.example.bookmycut.dtos.ProcedureDto;

import java.util.List;

public interface ProcedureCatalogService {

    ProcedureDto createProcedure(ProcedureDto procedureDto);

    List<ProcedureDto> getAllProcedures();

    ProcedureDto getProcedureById(Long id);

    void updateProcedure(Long id, ProcedureDto procedureDto);

    void deleteProcedure(Long id);
}
