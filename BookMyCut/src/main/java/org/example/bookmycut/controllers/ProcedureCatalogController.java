package org.example.bookmycut.controllers;

import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.services.contracts.ProcedureCatalogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedures")
public class ProcedureCatalogController {

    private final ProcedureCatalogService service;

    public ProcedureCatalogController(ProcedureCatalogService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProcedureDto addService(@RequestBody ProcedureDto dto) {
        return service.createProcedure(dto);
    }

    @GetMapping("/{id}")
    public ProcedureDto getService(@PathVariable Long id) {
        return service.getProcedureById(id);
    }

    @GetMapping
    public List<ProcedureDto> getAllServices() {
        return service.getAllProcedures();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProcedureDto updateService(@PathVariable Long id, @RequestBody ProcedureDto dto) {
        return service.updateProcedure(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProcedureDto removeService(@PathVariable Long id) {
        return null;
    }
}
