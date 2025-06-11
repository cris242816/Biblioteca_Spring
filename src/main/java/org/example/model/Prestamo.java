package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "prestamos")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de préstamo es obligatoria")
    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "revista_id")
    private Revista revista;

    @ManyToOne
    @JoinColumn(name = "dvd_id")
    private DVD dvd;

    private boolean devuelto = false;
}