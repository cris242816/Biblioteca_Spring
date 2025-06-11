package org.example.service;

import org.example.model.*;
import org.example.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {
    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private RevistaService revistaService;

    @Autowired
    private DVDService dvdService;

    public List<Prestamo> findAll() {
        return prestamoRepository.findAll();
    }

    public Optional<Prestamo> findById(Long id) {
        return prestamoRepository.findById(id);
    }

    public Prestamo save(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    public void deleteById(Long id) {
        prestamoRepository.deleteById(id);
    }

    public List<Prestamo> findByUsuarioId(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    public List<Prestamo> findByDevuelto(boolean devuelto) {
        return prestamoRepository.findByDevuelto(devuelto);
    }

    public Prestamo realizarPrestamo(Long usuarioId, Long libroId, Long revistaId, Long dvdId) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setDevuelto(false);

        if (libroId != null) {
            Libro libro = libroService.findById(libroId)
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
            if (!libro.isDisponible()) {
                throw new RuntimeException("El libro no está disponible");
            }
            libro.setDisponible(false);
            libroService.save(libro);
            prestamo.setLibro(libro);
        }

        if (revistaId != null) {
            Revista revista = revistaService.findById(revistaId)
                    .orElseThrow(() -> new RuntimeException("Revista no encontrada"));
            if (!revista.isDisponible()) {
                throw new RuntimeException("La revista no está disponible");
            }
            revista.setDisponible(false);
            revistaService.save(revista);
            prestamo.setRevista(revista);
        }

        if (dvdId != null) {
            DVD dvd = dvdService.findById(dvdId)
                    .orElseThrow(() -> new RuntimeException("DVD no encontrado"));
            if (!dvd.isDisponible()) {
                throw new RuntimeException("El DVD no está disponible");
            }
            dvd.setDisponible(false);
            dvdService.save(dvd);
            prestamo.setDvd(dvd);
        }

        return prestamoRepository.save(prestamo);
    }

    public Prestamo devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setDevuelto(true);

        if (prestamo.getLibro() != null) {
            Libro libro = prestamo.getLibro();
            libro.setDisponible(true);
            libroService.save(libro);
        }

        if (prestamo.getRevista() != null) {
            Revista revista = prestamo.getRevista();
            revista.setDisponible(true);
            revistaService.save(revista);
        }

        if (prestamo.getDvd() != null) {
            DVD dvd = prestamo.getDvd();
            dvd.setDisponible(true);
            dvdService.save(dvd);
        }

        return prestamoRepository.save(prestamo);
    }
}