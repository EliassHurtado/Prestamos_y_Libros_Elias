package mx.edu.utez.Prestamos_y_Libros.service;

import mx.edu.utez.Prestamos_y_Libros.dto.*;
import mx.edu.utez.Prestamos_y_Libros.exception.BadRequestException;
import mx.edu.utez.Prestamos_y_Libros.exception.ResourceNotFoundException;
import mx.edu.utez.Prestamos_y_Libros.model.*;
import mx.edu.utez.Prestamos_y_Libros.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LectorRepository lectorRepository,
                           LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public PrestamoResponseDTO solicitarPrestamo(SolicitudPrestamoDTO dto) {
        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new ResourceNotFoundException("El lector no existe con ID: " + dto.getLectorId()));

        if (!lector.isActivo()) {
            throw new BadRequestException("El lector no está activo para realizar préstamos.");
        }

        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("El libro no existe con ID: " + dto.getLibroId()));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new BadRequestException("El libro no tiene ejemplares disponibles.");
        }

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        long prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(lector.getId(), estadosActivos);
        int limiteMaximo = lector.isMembresiaPremium() ? 5 : 3;

        if (prestamosActivos >= limiteMaximo) {
            throw new BadRequestException("El lector excede el límite permitido de préstamos activos (" + limiteMaximo + ").");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = Prestamo.builder()
                .lector(lector)
                .libro(libro)
                .fechaPrestamo(LocalDate.now())
                .fechaDevolucionMaxima(LocalDate.now().plusDays(14))
                .estado(EstadoPrestamo.PRESTADO)
                .build();

        return mapToResponseDTO(prestamoRepository.save(prestamo));
    }

    @Transactional
    public PrestamoResponseDTO devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe con ID: " + prestamoId));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("El préstamo ya se encuentra DEVUELTO.");
        }

        LocalDate hoy = LocalDate.now();

        if (hoy.isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        return mapToResponseDTO(prestamoRepository.save(prestamo));
    }

    @Transactional
    public PrestamoResponseDTO renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe con ID: " + prestamoId));

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("No es posible renovar un préstamo en estado: " + prestamo.getEstado());
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return mapToResponseDTO(prestamoRepository.save(prestamo));
    }

    @Transactional
    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new ResourceNotFoundException("El lector no existe con ID: " + lectorId));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new ResourceNotFoundException("El lector no existe con ID: " + lectorId);
        }

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        return prestamoRepository.findByLectorIdAndEstadoIn(lectorId, estadosActivos)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Lector crearLector(LectorDTO dto) {
        Lector lector = Lector.builder()
                .nombre(dto.getNombre())
                .membresiaPremium(dto.isMembresiaPremium())
                .activo(true)
                .build();
        return lectorRepository.save(lector);
    }

    public Libro crearLibro(LibroDTO dto) {
        Libro libro = Libro.builder()
                .titulo(dto.getTitulo())
                .ejemplaresDisponibles(dto.getEjemplaresDisponibles())
                .build();
        return libroRepository.save(libro);
    }

    private PrestamoResponseDTO mapToResponseDTO(Prestamo p) {
        return PrestamoResponseDTO.builder()
                .id(p.getId())
                .lectorId(p.getLector().getId())
                .lectorNombre(p.getLector().getNombre())
                .libroId(p.getLibro().getId())
                .libroTitulo(p.getLibro().getTitulo())
                .fechaPrestamo(p.getFechaPrestamo())
                .fechaDevolucionMaxima(p.getFechaDevolucionMaxima())
                .estado(p.getEstado())
                .build();
    }
}