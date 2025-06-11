package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotBlank(message = "La editorial es obligatoria")
    private String editorial;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1000, message = "El año debe ser válido")
    @Max(value = 2100, message = "El año debe ser válido")
    private Integer anioPublicacion;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(unique = true)
    private String isbn;

    private boolean disponible = true;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}