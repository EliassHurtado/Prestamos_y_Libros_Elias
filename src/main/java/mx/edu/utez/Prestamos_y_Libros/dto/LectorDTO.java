package mx.edu.utez.Prestamos_y_Libros.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LectorDTO {

    @NotBlank(message = "El nombre del lector no puede estar vacío")
    private String nombre;

    private boolean membresiaPremium;
}