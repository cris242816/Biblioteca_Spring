package org.example.service;

import org.example.model.DVD;
import org.example.repository.DVDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DVDService {
    @Autowired
    private DVDRepository dvdRepository;

    public List<DVD> findAll() {
        return dvdRepository.findAll();
    }

    public Optional<DVD> findById(Long id) {
        return dvdRepository.findById(id);
    }

    public DVD save(DVD dvd) {
        return dvdRepository.save(dvd);
    }

    public void deleteById(Long id) {
        dvdRepository.deleteById(id);
    }

    public List<DVD> buscarPorTitulo(String titulo) {
        return dvdRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<DVD> buscarPorDirector(String director) {
        return dvdRepository.findByDirectorContainingIgnoreCase(director);
    }

    public List<DVD> buscarDisponibles() {
        return dvdRepository.findByDisponible(true);
    }
}