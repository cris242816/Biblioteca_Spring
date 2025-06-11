package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "revistas")
public class Revista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La editorial es obligatoria")
    private String editorial;

    @NotNull(message = "El número es obligatorio")
    @Min(value = 1, message = "El número debe ser positivo")
    private Integer numero;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1000, message = "El año debe ser válido")
    @Max(value = 2100, message = "El año debe ser válido")
    private Integer anio;

    @NotBlank(message = "El ISSN es obligatorio")
    @Column(unique = true)
    private String issn;

    private boolean disponible = true;
}