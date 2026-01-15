package org.example.bookmycut.exceptions;

public class ProcedureHasAppointmentsException extends RuntimeException {

    public ProcedureHasAppointmentsException(Long procedureId) {
        super(String.format(
                "Cannot delete Procedure with id %d because it has existing appointments.",
                procedureId
        ));
    }
}
