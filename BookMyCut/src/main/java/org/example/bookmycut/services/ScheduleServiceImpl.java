package org.example.bookmycut.services;

import org.example.bookmycut.dtos.ScheduleDto;
import org.example.bookmycut.exceptions.EntityNotFoundException;
import org.example.bookmycut.exceptions.TimeOverlapException;
import org.example.bookmycut.helpers.mappers.EmployeeScheduleMapper;
import org.example.bookmycut.models.Employee;
import org.example.bookmycut.models.EmployeeSchedule;
import org.example.bookmycut.repositories.EmployeeRepository;
import org.example.bookmycut.repositories.EmployeeScheduleRepository;
import org.example.bookmycut.services.contracts.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final String OVERLAP_MESSAGE = "The new schedule overlaps with an existing schedule for this employee.";

    private final String START_BEFORE_END_MESSAGE = "Start time must be before end time";

    private final EmployeeScheduleRepository scheduleRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeScheduleMapper scheduleMapper;

    @Autowired
    public ScheduleServiceImpl(EmployeeScheduleRepository scheduleRepository,
                               EmployeeRepository employeeRepository,
                               EmployeeScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleMapper = scheduleMapper;
    }

    @Transactional
    @Override
    public ScheduleDto addSchedule(ScheduleDto scheduleDto) {

        if (!scheduleDto.getStartTime().isBefore(scheduleDto.getEndTime())) {
            throw new IllegalArgumentException(START_BEFORE_END_MESSAGE);
        }

        Employee employee = employeeRepository.findById(scheduleDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", scheduleDto.getEmployeeId()));


        if(isOverlapping(scheduleDto)){
            throw new TimeOverlapException(OVERLAP_MESSAGE);
        }

        EmployeeSchedule saved = scheduleRepository.save(scheduleMapper.toEntity(scheduleDto,employee));

        return scheduleMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduleDto> getScheduleForEmployee(Long employeeId) {
        List<EmployeeSchedule> schedules = scheduleRepository.findByEmployeeIdOrderByDayOfWeekAscStartTimeAsc(employeeId);

        if(schedules.isEmpty() || !employeeRepository.existsById(employeeId)){
            throw new EntityNotFoundException("Employee", employeeId);
        }

        return schedules.stream()
                .map(scheduleMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public void removeSchedule(Long employeeId, DayOfWeek day) {
         EmployeeSchedule schedule = scheduleRepository
                 .findByEmployeeIdAndDayOfWeek(employeeId, day.getValue())
                 .orElseThrow(() -> new EntityNotFoundException(employeeId,
                         day));

         scheduleRepository.delete(schedule);
    }

    @Override
    public void updateSchedule(ScheduleDto scheduleDTO) {
        if(!employeeRepository.existsById(scheduleDTO.getEmployeeId())){
            throw new EntityNotFoundException("Employee", scheduleDTO.getEmployeeId());
        }

        EmployeeSchedule schedule = scheduleRepository
                .findByEmployeeIdAndDayOfWeek(scheduleDTO.getEmployeeId(), scheduleDTO.getDayOfWeek())
                .orElseThrow(() -> new EntityNotFoundException(scheduleDTO.getEmployeeId(),
                        DayOfWeek.of(scheduleDTO.getDayOfWeek())));

        if (isOverlappingExcluding(scheduleDTO, schedule.getId())) {
            throw new TimeOverlapException(OVERLAP_MESSAGE);
        }

        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());

        EmployeeSchedule saved = scheduleRepository.save(schedule);
    }


    @Transactional(readOnly = true)
    @Override
    public ScheduleDto getScheduleForEmployeeByDay(Long employeeId, DayOfWeek dayOfWeek) {
        if(!employeeRepository.existsById(employeeId)){
            throw new EntityNotFoundException("Employee", employeeId);
        }

        EmployeeSchedule schedule = scheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId,dayOfWeek.getValue())
                .orElseThrow(() -> new EntityNotFoundException(employeeId,dayOfWeek));
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(scheduleMapper::toDTO)
                .toList();
    }

    protected boolean isOverlapping(ScheduleDto scheduleDto){
        return scheduleRepository.existsByEmployeeIdAndDayOfWeekAndTimeOverlap(
                scheduleDto.getEmployeeId(),
                scheduleDto.getDayOfWeek(),
                scheduleDto.getStartTime(),
                scheduleDto.getEndTime()
        );
    }

    protected boolean isOverlappingExcluding(ScheduleDto scheduleDto, Long excludeId){
        return scheduleRepository.existsByEmployeeIdAndDayOfWeekAndTimeOverlapExcludingId(
                scheduleDto.getEmployeeId(),
                scheduleDto.getDayOfWeek(),
                scheduleDto.getStartTime(),
                scheduleDto.getEndTime(),
                excludeId
        );
    }


}
