package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProcedureRepository extends
        JpaRepository<@NonNull Procedure, @NonNull Long> {

    boolean existsByName(String name);
}
