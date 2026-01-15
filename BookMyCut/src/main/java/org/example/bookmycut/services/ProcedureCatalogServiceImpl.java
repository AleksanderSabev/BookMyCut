package org.example.bookmycut.services;

import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.exceptions.ProcedureHasAppointmentsException;
import org.example.bookmycut.helpers.mappers.ProcedureMapper;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.repositories.AppointmentRepository;
import org.example.bookmycut.repositories.ProcedureRepository;
import org.example.bookmycut.services.contracts.ProcedureCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcedureCatalogServiceImpl implements ProcedureCatalogService {

    private final ProcedureRepository procedureRepository;

    private final AppointmentRepository appointmentRepository;

    private final ProcedureMapper mapper;

    @Autowired
    public ProcedureCatalogServiceImpl(ProcedureRepository procedureRepository, AppointmentRepository appointmentRepository,
                                       ProcedureMapper mapper) {
        this.procedureRepository = procedureRepository;
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public ProcedureDto createProcedure(ProcedureDto procedureDto) {
        Procedure procedure = mapper.toEntity(procedureDto);

        return mapper.toDto(procedureRepository.save(procedure));
    }

    @Override
    public List<ProcedureDto> getAllProcedures() {
        return procedureRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProcedureDto getProcedureById(Long id) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procedure", id));

        return mapper.toDto(procedure);
    }

    @Transactional
    @Override
    public ProcedureDto updateProcedure(Long id, ProcedureDto procedureDto) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procedure", id));

        procedure.setName(procedureDto.getName());
        procedure.setDurationMinutes(procedureDto.getDurationInMinutes());
        procedure.setPrice(procedureDto.getPrice());

        Procedure saved = procedureRepository.save(procedure);
        return mapper.toDto(saved);
    }

    @Transactional
    @Override
    public void deleteProcedure(Long id) {
        if(!procedureRepository.existsById(id))
            throw new EntityNotFoundException("Procedure", id);

        if(appointmentRepository.existsByProcedureId(id)){
            throw new ProcedureHasAppointmentsException(id);
        }

        procedureRepository.deleteById(id);
    }
}
