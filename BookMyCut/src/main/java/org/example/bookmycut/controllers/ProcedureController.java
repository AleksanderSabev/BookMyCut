package org.example.bookmycut.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.services.contracts.ProcedureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    private static final String UPDATE_SUCCESSFUL = "Procedure updated successfully.";
    private static final String DELETE_SUCCESSFUL = "Procedure deleted successfully.";

    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull ProcedureDto> createProcedure(@Valid @RequestBody ProcedureDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(procedureService.createProcedure(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull ProcedureDto> getProcedure(@PathVariable Long id) {
        return ResponseEntity.ok(procedureService.getProcedureById(id));
    }

    @GetMapping
    public List<ProcedureDto> getAllProcedures() {
        return procedureService.getAllProcedures();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> updateProcedure(@PathVariable Long id, @Valid @RequestBody ProcedureDto dto) {
        procedureService.updateProcedure(id, dto);
        return ResponseEntity.ok(UPDATE_SUCCESSFUL);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull String> removeProcedure(@PathVariable Long id) {
        procedureService.removeProcedure(id);
        return ResponseEntity.ok(DELETE_SUCCESSFUL);
    }
}
