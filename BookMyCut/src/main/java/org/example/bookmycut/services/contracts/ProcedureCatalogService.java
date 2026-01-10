package org.example.bookmycut.services.contracts;

import org.example.bookmycut.models.Procedure;

import java.util.List;
import java.util.Optional;

public interface ProcedureCatalogService {

    Procedure createService(Procedure procedure);

    List<Procedure> getAllServices();

    Optional<Procedure> getServiceById(Long id);
}
