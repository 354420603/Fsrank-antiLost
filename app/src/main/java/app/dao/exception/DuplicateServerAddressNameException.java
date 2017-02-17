package app.dao.exception;

public class DuplicateServerAddressNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateServerAddressNameException(String message) {
        super(message);
    }
}
