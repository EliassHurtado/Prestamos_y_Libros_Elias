package mx.edu.utez.Prestamos_y_Libros.controller;

import jakarta.validation.Valid;
import mx.edu.utez.Prestamos_y_Libros.dto.LectorDTO;
import mx.edu.utez.Prestamos_y_Libros.model.Lector;
import mx.edu.utez.Prestamos_y_Libros.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectores")
public class LectorController {

    private final PrestamoService prestamoService;

    public LectorController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Lector> crearLector(@Valid @RequestBody LectorDTO dto) {
        return new ResponseEntity<>(prestamoService.crearLector(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.rehabilitarLector(id));
    }
}