package mx.edu.utez.Prestamos_y_Libros.repository;

import mx.edu.utez.Prestamos_y_Libros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
}