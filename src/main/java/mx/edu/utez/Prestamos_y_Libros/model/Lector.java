package mx.edu.utez.Prestamos_y_Libros.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lectores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean membresiaPremium = false;
}