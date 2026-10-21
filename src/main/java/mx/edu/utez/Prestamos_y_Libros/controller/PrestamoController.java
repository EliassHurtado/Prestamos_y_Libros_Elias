package mx.edu.utez.Prestamos_y_Libros.controller;

import jakarta.validation.Valid;
import mx.edu.utez.Prestamos_y_Libros.dto.PrestamoResponseDTO;
import mx.edu.utez.Prestamos_y_Libros.dto.SolicitudPrestamoDTO;
import mx.edu.utez.Prestamos_y_Libros.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> solicitarPrestamo(@Valid @RequestBody SolicitudPrestamoDTO dto) {
        return new ResponseEntity<>(prestamoService.solicitarPrestamo(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<PrestamoResponseDTO> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(id));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}