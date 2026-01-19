package org.example.bookmycut.exceptions;

public class EntityHasAppointmentsException extends RuntimeException {

    public EntityHasAppointmentsException(String type, Long procedureId) {
        super(String.format(
                "Cannot delete %s with id %d because it has existing appointments.",
                type,
                procedureId
        ));
    }

    public EntityHasAppointmentsException(Long employeeId) {
        super(String.format(
                "Cannot add time off because employee with id %d has existing appointments.",
                employeeId
        ));
    }
}
