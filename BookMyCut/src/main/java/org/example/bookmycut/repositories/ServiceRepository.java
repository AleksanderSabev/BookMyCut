package org.example.bookmycut.repositories;

import lombok.NonNull;
import org.example.bookmycut.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends
        JpaRepository<@NonNull Service, @NonNull Long> {

    Optional<Service> findByName(String name);

    boolean existsByName(String name);
}
