package org.example.bookmycut.exceptions;

public class EntityHasAppointmentsException extends RuntimeException {

    public EntityHasAppointmentsException(String type, Long procedureId) {
        super(String.format(
                "Cannot delete %S with id %d because it has existing appointments.",
                type,
                procedureId
        ));
    }
}
