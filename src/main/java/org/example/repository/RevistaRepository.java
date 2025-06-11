package org.example.repository;

import org.example.model.Revista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevistaRepository extends JpaRepository<Revista, Long> {
    List<Revista> findByTituloContainingIgnoreCase(String titulo);
    List<Revista> findByDisponible(boolean disponible);
    boolean existsByIssn(String issn);
}