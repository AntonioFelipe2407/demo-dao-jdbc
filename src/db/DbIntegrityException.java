package db;

// Exceção personalizada para integridade referêncial
public class DbIntegrityException extends RuntimeException {

    public DbIntegrityException(String message) {
        super(message);
    }
}
