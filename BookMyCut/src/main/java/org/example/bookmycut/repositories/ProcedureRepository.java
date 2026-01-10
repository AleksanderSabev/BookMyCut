package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcedureRepository extends
        JpaRepository<@NonNull Procedure, @NonNull Long> {

    Optional<Procedure> findByName(String name);

    boolean existsByName(String name);
}
