package lab2.task4.server;

public class BadInputException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadInputException(String message) {
        super(message);
    }
}
