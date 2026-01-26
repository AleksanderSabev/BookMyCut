package org.example.bookmycut.exceptions;

import java.time.DayOfWeek;
import java.util.List;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.",type, attribute,value));
    }

    public EntityNotFoundException(String type,Long id){
        this(type, "id",String.valueOf(id));
    }

    public EntityNotFoundException(String type, String attribute, List<Long> missingIds) {
        super(String.format("%s(s) with %s(s) %s not found.", type, attribute, missingIds));
    }

    public EntityNotFoundException(String type, List<Long> missingIds) {
        this(type, "id", missingIds);
    }

    public EntityNotFoundException(Long id, DayOfWeek dayOfWeek) {
        super(String.format("Schedule of employee with id %d for %s not found", id, dayOfWeek));
    }
}
