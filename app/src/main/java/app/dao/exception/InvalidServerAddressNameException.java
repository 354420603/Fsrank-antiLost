package app.dao.exception;

public class InvalidServerAddressNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidServerAddressNameException(String message) {
        super(message);
    }
}
