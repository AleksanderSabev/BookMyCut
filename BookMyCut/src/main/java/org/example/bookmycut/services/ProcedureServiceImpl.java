package org.example.bookmycut.services;

import org.example.bookmycut.dtos.ProcedureDto;
import org.example.bookmycut.exceptions.DuplicateEntityException;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.exceptions.EntityHasAppointmentsException;
import org.example.bookmycut.helpers.mappers.ProcedureMapper;
import org.example.bookmycut.models.Procedure;
import org.example.bookmycut.repositories.AppointmentRepository;
import org.example.bookmycut.repositories.ProcedureRepository;
import org.example.bookmycut.services.contracts.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    private final ProcedureRepository procedureRepository;

    private final AppointmentRepository appointmentRepository;

    private final ProcedureMapper mapper;

    @Autowired
    public ProcedureServiceImpl(ProcedureRepository procedureRepository, AppointmentRepository appointmentRepository,
                                ProcedureMapper mapper) {
        this.procedureRepository = procedureRepository;
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public ProcedureDto createProcedure(ProcedureDto procedureDto) {
        if(procedureRepository.existsByName(procedureDto.getName())){
            throw new DuplicateEntityException("Procedure", "name", procedureDto.getName());
        }

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
    public void updateProcedure(Long id, ProcedureDto procedureDto) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procedure", id));

        procedure.setName(procedureDto.getName());
        procedure.setDurationMinutes(procedureDto.getDurationInMinutes());
        procedure.setPrice(procedureDto.getPrice());

        Procedure saved = procedureRepository.save(procedure);
    }

    @Transactional
    @Override
    public void removeProcedure(Long id) {
        if(!procedureRepository.existsById(id))
            throw new EntityNotFoundException("Procedure", id);

        if(appointmentRepository.existsByProcedureId(id)){
            throw new EntityHasAppointmentsException("Procedure",id);
        }

        procedureRepository.deleteById(id);
    }
}
