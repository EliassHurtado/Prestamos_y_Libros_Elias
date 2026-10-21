package mx.edu.utez.Prestamos_y_Libros.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LibroDTO {

    @NotBlank(message = "El título del libro no puede estar vacío")
    private String titulo;

    @Min(value = 0, message = "Los ejemplares disponibles no pueden ser números negativos")
    private int ejemplaresDisponibles;
}