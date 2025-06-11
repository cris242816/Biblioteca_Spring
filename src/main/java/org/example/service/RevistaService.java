package org.example.service;

import org.example.model.Revista;
import org.example.repository.RevistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RevistaService {
    @Autowired
    private RevistaRepository revistaRepository;

    public List<Revista> findAll() {
        return revistaRepository.findAll();
    }

    public Optional<Revista> findById(Long id) {
        return revistaRepository.findById(id);
    }

    public Revista save(Revista revista) {
        return revistaRepository.save(revista);
    }

    public void deleteById(Long id) {
        revistaRepository.deleteById(id);
    }

    public List<Revista> buscarPorTitulo(String titulo) {
        return revistaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Revista> buscarDisponibles() {
        return revistaRepository.findByDisponible(true);
    }

    public boolean existsByIssn(String issn) {
        return revistaRepository.existsByIssn(issn);
    }
}