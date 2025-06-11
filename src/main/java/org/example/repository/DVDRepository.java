package org.example.repository;

import org.example.model.DVD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DVDRepository extends JpaRepository<DVD, Long> {
    List<DVD> findByTituloContainingIgnoreCase(String titulo);
    List<DVD> findByDirectorContainingIgnoreCase(String director);
    List<DVD> findByDisponible(boolean disponible);
}