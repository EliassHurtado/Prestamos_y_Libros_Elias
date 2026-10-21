package mx.edu.utez.Prestamos_y_Libros.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}