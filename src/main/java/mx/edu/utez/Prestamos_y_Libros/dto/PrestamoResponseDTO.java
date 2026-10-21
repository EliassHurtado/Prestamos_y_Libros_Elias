package mx.edu.utez.Prestamos_y_Libros.dto;

import lombok.Builder;
import lombok.Data;
import mx.edu.utez.Prestamos_y_Libros.model.EstadoPrestamo;

import java.time.LocalDate;

@Data
@Builder
public class PrestamoResponseDTO {
    private Long id;
    private Long lectorId;
    private String lectorNombre;
    private Long libroId;
    private String libroTitulo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private EstadoPrestamo estado;
}