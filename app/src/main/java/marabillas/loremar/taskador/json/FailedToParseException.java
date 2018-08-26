package marabillas.loremar.taskador.json;

/**
 * This exception is thrown when exception occured while parsing data
 */
public class FailedToParseException extends Exception {
    public FailedToParseException() {
    }

    public FailedToParseException(String message) {
        super(message);
    }

    public FailedToParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToParseException(Throwable cause) {
        super(cause);
    }
}