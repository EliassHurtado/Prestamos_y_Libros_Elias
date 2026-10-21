package mx.edu.utez.Prestamos_y_Libros.repository;

import mx.edu.utez.Prestamos_y_Libros.model.EstadoPrestamo;
import mx.edu.utez.Prestamos_y_Libros.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
}