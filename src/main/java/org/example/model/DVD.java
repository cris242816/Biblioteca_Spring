package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "dvds")
public class DVD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El director es obligatorio")
    private String director;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser válido")
    @Max(value = 2100, message = "El año debe ser válido")
    private Integer anio;

    @NotBlank(message = "La duración es obligatoria")
    private String duracion;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    private boolean disponible = true;
}