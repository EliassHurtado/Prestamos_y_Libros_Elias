package mx.edu.utez.Prestamos_y_Libros.controller;

import jakarta.validation.Valid;
import mx.edu.utez.Prestamos_y_Libros.dto.LibroDTO;
import mx.edu.utez.Prestamos_y_Libros.model.Libro;
import mx.edu.utez.Prestamos_y_Libros.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final PrestamoService prestamoService;

    public LibroController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody LibroDTO dto) {
        return new ResponseEntity<>(prestamoService.crearLibro(dto), HttpStatus.CREATED);
    }
}